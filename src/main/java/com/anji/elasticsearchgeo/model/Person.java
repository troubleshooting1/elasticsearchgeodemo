package com.anji.elasticsearchgeo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.Serializable;

/**
 * Description:
 * author: chenqiang
 * date: 2018/6/5 15:21
 */
@Document(indexName = "elastic_search_project",type = "person",indexStoreType = "fs",shards = 5,replicas = 1,refreshInterval = "-1")
public class Person implements Serializable {
    @Id
    private int id;

    private String name;

    private String phone;

    @GeoPointField
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
