package com.project.repository.elastic;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapper.ZadanieMapper;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.model.ZadanieES;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ZadanieRepositoryES {
    
    private final RestHighLevelClient esClient;
    private final Logger logger = LoggerFactory.getLogger(ZadanieRepositoryES.class);
    private final ObjectMapper mapper;
    
    @Autowired
    private ZadanieMapper zadanieMapper;
    
    public ZadanieRepositoryES(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        
        String clusterUrl = "http://127.0.0.1:9200";
        final CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","changeme"));
        this.esClient = new RestHighLevelClient(RestClient.builder(HttpHost.create(clusterUrl))
        .setHttpClientConfigCallback(hcb -> hcb.setDefaultCredentialsProvider(cp)));

    }
    
    public void save(Zadanie zadanie) throws IOException {
        String json = mapper.writeValueAsString(zadanie);
        esClient.index(new IndexRequest("zadanie").source(json, XContentType.JSON), RequestOptions.DEFAULT);
    }

    public void delete(Integer zadanieId) throws IOException {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest("zadanie");
        deleteRequest.setQuery(QueryBuilders.matchQuery("zadanieId", zadanieId));
        System.out.println("dr: " + deleteRequest.getBatchSize());
        esClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
    }
    
    public void update(Zadanie zadanie) throws IOException {
        String json = mapper.writeValueAsString(zadanie);
        esClient.update(new UpdateRequest("zadanie", "" + zadanie.getZadanieId()).doc(json, XContentType.JSON), RequestOptions.DEFAULT);
    }

    public Page<Zadanie> search(QueryBuilder query, Integer from, Integer size, Pageable pageable) throws IOException {
        SearchResponse response = esClient.search(new SearchRequest("zadanie")
                .source(new SearchSourceBuilder()
                        .query(query)
                        .from(from)
                        .size(size)
                        .trackTotalHits(true)
        ), RequestOptions.DEFAULT);

        SearchHits searchHits = response.getHits();

        List<Zadanie> resultList = new ArrayList<>();
        for(SearchHit hit : searchHits) {
            System.out.println(hit);
             ZadanieES zadanieEntity = mapper.readValue(hit.getSourceAsString(),ZadanieES.class);
            
            
//            Projekt projekt = null;
//            Student student = null;
//            zadanieService.getZadanieById(zadanieEntity.getZadanieId()).get().getProjekt();
//            zadanieEntity.setProjekt(projekt);
//            zadanieEntity.setStudent(student);
            resultList.add(zadanieMapper.zadanieESToZadanie(zadanieEntity));
            //Zadanie zadanieEntity = mapper.readValue(hit.getSourceAsString(),Zadanie.class);
            //resultList.add(zadanieEntity);
        }
        
        return new PageImpl<>(resultList, PageRequest.of(from,size), searchHits.getTotalHits().value);
    }
    
}
