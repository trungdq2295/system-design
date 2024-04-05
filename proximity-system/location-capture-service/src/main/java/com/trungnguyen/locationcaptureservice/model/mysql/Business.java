package com.trungnguyen.locationcaptureservice.model.mysql;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema = "business", name = "business_information")
@Data
public class Business {

    @Id
    @Column(name ="business_id")
    private String id;

    @Column
    private String name;

    @Column
    private String businessType;

    @Column
    private String description;
}
