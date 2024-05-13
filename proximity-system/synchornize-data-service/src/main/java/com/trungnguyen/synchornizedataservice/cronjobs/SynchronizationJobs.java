package com.trungnguyen.synchornizedataservice.cronjobs;


import com.trungnguyen.synchornizedataservice.enumeration.BusinessType;
import com.trungnguyen.synchornizedataservice.model.ElasticsearchShard;
import com.trungnguyen.synchornizedataservice.model.cassandra.Location;
import com.trungnguyen.synchornizedataservice.model.elasticsearch.LocationIndex;
import com.trungnguyen.synchornizedataservice.repository.ElasticSearchConnectionRepository;
import com.trungnguyen.synchornizedataservice.repository.cassandra.LocationRepository;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
            Optional<List<Location>> data = locationRepository.findAllByTimestampAfter(shard.getLastUpdateDate().getTime());
            if(data.isPresent() && !data.get().isEmpty()){
                var template = createElasticSearchTemplateConnection(shard);
                for(Location location : data.get()){
                    LocationIndex indexData = new LocationIndex();
                    indexData.setId(location.getBusiness_id());
                    indexData.setGeoPoint(new GeoPoint(location.getLatitude(), location.getLongitude()));
                    indexData.setType(BusinessType.GAS_STATION);
                    template.save(indexData, IndexCoordinates.of("location"));
                }
            }
        }

    }


    private ElasticsearchRestTemplate createElasticSearchTemplateConnection(ElasticsearchShard shard){
        RestClientBuilder client = RestClient.builder(
                        new HttpHost(shard.getIpAdress(), shard.getPort(), "http"))
                .setDefaultHeaders(compatibilityHeaders())
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(
                                new BasicCredentialsProvider()));
        RestHighLevelClient highLevelClient = new RestHighLevelClient(client);
        return new ElasticsearchRestTemplate(highLevelClient);
    }

    private Header[] compatibilityHeaders() {
        return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"), new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
    }
}
