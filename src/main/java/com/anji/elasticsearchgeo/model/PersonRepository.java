package com.anji.elasticsearchgeo.model;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Description:
 * author: chenqiang
 * date: 2018/6/5 15:23
 */
public interface PersonRepository extends ElasticsearchRepository<Person,Integer> {
}
