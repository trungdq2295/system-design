package com.trungnguyen.synchornizedataservice.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(schema = "synchronization", name = "es_shard_location")
@Data
public class ElasticsearchShard {


    @Id
    @GeneratedValue
    @Column(name = "es_id")
    private Long id;


    @Column(name ="es_name")
    private String name;

    @Column(name ="es_ip_address")
    private String ipAdress;

    @Column(name = "es_port")
    private int port;

    @Column(name ="last_update_date")
    private Date lastUpdateDate;
}
