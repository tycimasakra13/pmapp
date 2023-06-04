package com.project.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Projekt;
import java.io.IOException;
import java.sql.Timestamp;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchDao {
    
    private final RestHighLevelClient esClient;
    private final Logger logger = LoggerFactory.getLogger(ElasticsearchDao.class);
    private final ObjectMapper mapper;
    private final BulkProcessor bulkProcessor;
    private Timestamp ts;

    public ElasticsearchDao(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        
        String clusterUrl = "http://127.0.0.1:9200";
        final CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","changeme"));
        this.esClient = new RestHighLevelClient(RestClient.builder(HttpHost.create(clusterUrl))
        .setHttpClientConfigCallback(hcb -> hcb.setDefaultCredentialsProvider(cp)));
        
        MainResponse info = esClient.info(RequestOptions.DEFAULT);
        logger.info("C to {} running v {}", clusterUrl, info.getVersion().getNumber());
        
                this.bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) -> esClient.bulkAsync(
                        request, RequestOptions.DEFAULT, bulkListener),
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long l, BulkRequest br) {
                        logger.debug("before bulk {}", br.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long l, BulkRequest br, BulkResponse br1) {
                        logger.debug("after bulk {} fail", br1.hasFailures() ? "with" : "without");
                    }

                    @Override
                    public void afterBulk(long l, BulkRequest br, Throwable thrwbl) {
                        logger.warn("error bulk {}", thrwbl);
                    }
                })
                .setBulkActions(1000)
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .build();
    }

    public void save(Projekt projekt) throws IOException {
        projekt.setProjektId(ts.getTime());
        String json = mapper.writeValueAsString(projekt);
        esClient.index(new IndexRequest("projekt").id(projekt.idAsString()).source(json, XContentType.JSON), RequestOptions.DEFAULT);
    }

    public void delete(Integer projektId) throws IOException {
        esClient.delete(new DeleteRequest("projekt", "" + projektId), RequestOptions.DEFAULT);
    }
    
}
