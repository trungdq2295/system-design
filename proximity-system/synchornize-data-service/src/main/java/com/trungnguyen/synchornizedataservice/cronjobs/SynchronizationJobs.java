package com.trungnguyen.synchornizedataservice.cronjobs;


import com.trungnguyen.synchornizedataservice.model.ElasticsearchShard;
import com.trungnguyen.synchornizedataservice.model.cassandra.Location;
import com.trungnguyen.synchornizedataservice.repository.ElasticSearchConnectionRepository;
import com.trungnguyen.synchornizedataservice.repository.cassandra.LocationRepository;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SynchronizationJobs {


    private ElasticSearchConnectionRepository repository;

    private LocationRepository locationRepository;

    public SynchronizationJobs(ElasticSearchConnectionRepository repository, LocationRepository locationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
    }

    public void synchronizeData(){
        List<ElasticsearchShard> list = repository.findAll();
        for(ElasticsearchShard shard : list){
            Optional<List<Location>> data = locationRepository.findAllByTimestampAfter(shard.getLastUpdateDate());
            if(data.isPresent() && !data.get().isEmpty()){
                var template = createElasticSearchTemplateConnection(shard);

            }
        }

    }


    private ElasticsearchRestTemplate createElasticSearchTemplateConnection(ElasticsearchShard shard){
        RestClientBuilder client = RestClient.builder(
                        new HttpHost(shard.getIpAdress(), shard.getPort(), "http"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(
                                new BasicCredentialsProvider()));
        RestHighLevelClient highLevelClient = new RestHighLevelClient(client);
        return new ElasticsearchRestTemplate(highLevelClient);
    }
}
