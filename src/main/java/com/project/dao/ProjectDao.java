package com.project.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Projekt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProjectDao {
    
    private final RestHighLevelClient esClient;
    private final Logger logger = LoggerFactory.getLogger(ProjectDao.class);
    private final ObjectMapper mapper;
    
    public ProjectDao(ObjectMapper mapper) throws IOException {
        this.mapper = mapper;
        
        String clusterUrl = "http://127.0.0.1:9200";
        final CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","changeme"));
        this.esClient = new RestHighLevelClient(RestClient.builder(HttpHost.create(clusterUrl))
        .setHttpClientConfigCallback(hcb -> hcb.setDefaultCredentialsProvider(cp)));

    }
    
    public void save(Projekt projekt) throws IOException {
        String json = mapper.writeValueAsString(projekt);
        esClient.index(new IndexRequest("projekt").source(json, XContentType.JSON), RequestOptions.DEFAULT);
    }

    public void delete(Integer projektId) throws IOException {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest("projekt");
        deleteRequest.setQuery(QueryBuilders.matchQuery("projektId", projektId));
        System.out.println("dr: " + deleteRequest.getBatchSize());
        esClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
    }
    
    public void update(Projekt projekt, String documentId) throws IOException {
        String json = mapper.writeValueAsString(projekt);
        esClient.update(new UpdateRequest("projekt", "" + documentId).doc(json, XContentType.JSON), RequestOptions.DEFAULT);
    }

    public List<String> getDocId(QueryBuilder query) throws IOException {
        SearchResponse response = esClient.search(new SearchRequest("projekt")
            .source(new SearchSourceBuilder()
                .query(query)
                .trackTotalHits(true)
            ), RequestOptions.DEFAULT);
        
        System.out.println("hits: " + response.toString());
        SearchHits searchHits = response.getHits();
        
        List<String> resultList = new ArrayList<>();
        for(SearchHit hit : searchHits) {
            System.out.println("doc index" + hit.getIndex());
            System.out.println("doc getid" + hit.getId());
            resultList.add(hit.getId());
        }
        return resultList;
    }
    
    public Page<Projekt> search(QueryBuilder query, Integer from, Integer size, Pageable pageable) throws IOException {
        SearchResponse response = esClient.search(new SearchRequest("projekt")
                .source(new SearchSourceBuilder()
                        .query(query)
                        .from(from)
                        .size(size)
                        .trackTotalHits(true)
        ), RequestOptions.DEFAULT);

        SearchHits searchHits = response.getHits();

        List<Projekt> resultList = new ArrayList<>();
        for(SearchHit hit : searchHits) {
            System.out.println(hit);
            Projekt projektEntity = mapper.readValue(hit.getSourceAsString(),Projekt.class);
            resultList.add(projektEntity);
        }
        
        return new PageImpl<>(resultList, PageRequest.of(from,size), searchHits.getTotalHits().value);
    }
    
}
