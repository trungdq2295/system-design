package com.trungnguyen.synchornizedataservice.unit;


import com.trungnguyen.synchornizedataservice.cronjobs.SynchronizationJobs;
import com.trungnguyen.synchornizedataservice.model.ElasticsearchShard;
import com.trungnguyen.synchornizedataservice.model.cassandra.Location;
import com.trungnguyen.synchornizedataservice.repository.ElasticSearchConnectionRepository;
import com.trungnguyen.synchornizedataservice.repository.cassandra.LocationRepository;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SynchronizationJobTest {


    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ElasticSearchConnectionRepository repository;

    @Mock
    RestHighLevelClient restHighLevelClient;

    @Mock
    RestClientBuilder restClientBuilder;

    private SynchronizationJobs synchronizationJobs;

    @Mock
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @BeforeEach
    public void setup(){
        synchronizationJobs = new SynchronizationJobs(repository, locationRepository);
    }

    @Test
    public void givenWhen_ThereIsNoDataUpdatedAfterShard_ThenNoUpdate(){
        Date currentTime = new Date();
        List<ElasticsearchShard> list = new ArrayList<>();
        ElasticsearchShard shard = new ElasticsearchShard();
        shard.setId(12_3L);
        shard.setName("Test ElasticShard");
        shard.setPort(456);
        shard.setLastUpdateDate(currentTime);
        list.add(shard);

        ElasticsearchShard savedShard = new ElasticsearchShard();
        savedShard.setId(12_3L);
        savedShard.setName("Test ElasticShard");
        savedShard.setPort(456);
        savedShard.setLastUpdateDate(new Date());
        when(repository.findAll()).thenReturn(list);
        synchronizationJobs.synchronizeData();
        assertThat(shard.getLastUpdateDate()).isEqualTo(currentTime);
    }

    @Test
    public void givenWhen_ThereDataUpdatedAfterShard_ThenUpdate() throws IOException {
        Date currentTime = new Date();
        List<ElasticsearchShard> listShard = new ArrayList<>();
        ElasticsearchShard shard = new ElasticsearchShard();
        shard.setId(12_3L);
        shard.setName("Test ElasticShard");
        shard.setIpAdress("localhost");
        shard.setPort(456);
        shard.setLastUpdateDate(currentTime);
        listShard.add(shard);
        ElasticsearchShard savedShard = new ElasticsearchShard();
        savedShard.setId(12_3L);
        savedShard.setName("Test ElasticShard");
        savedShard.setPort(456);
        savedShard.setLastUpdateDate(new Date());

        //Cassandra
        Location location = new Location();
        location.setBusiness_id("Test");
        List<Location> listLocation = new ArrayList<>();
        listLocation.add(location);

        when(repository.findAll()).thenReturn(listShard);
        when(repository.save(any())).thenReturn(savedShard);
        when(locationRepository.findAllByTimestampAfter(shard.getLastUpdateDate().getTime())).thenReturn(Optional.of(listLocation));

        try(MockedConstruction<ElasticsearchRestTemplate> mockedConstruction = Mockito.mockConstruction(ElasticsearchRestTemplate.class)){
            synchronizationJobs.synchronizeData();
            assertThat(shard.getLastUpdateDate()).isAfter(currentTime);
        }
    }

}
