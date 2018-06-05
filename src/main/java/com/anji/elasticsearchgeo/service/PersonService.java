package com.anji.elasticsearchgeo.service;

import com.anji.elasticsearchgeo.model.Person;
import com.anji.elasticsearchgeo.model.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * author: chenqiang
 * date: 2018/6/5 15:24
 */
@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    private static final String PERSON_INDEX_NAME = "elastic_search_project";
    private static final String PERSON_INDEX_TYPE = "person";

    public Person add(Person person) {
        return personRepository.save(person);
    }

    public void bulkIndex(List<Person> personList) {
        int counter = 0;

        try {
            if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
                elasticsearchTemplate.createIndex(PERSON_INDEX_TYPE);
            }
            List<IndexQuery> queries = new ArrayList<>();
            for (Person person : personList) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(person.getId() + "");
                indexQuery.setObject(person);
                indexQuery.setIndexName(PERSON_INDEX_NAME);
                indexQuery.setType(PERSON_INDEX_TYPE);

                queries.add(indexQuery);
                //每逢500的整数倍就bulk插入一次
                if (counter % 500 == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    System.out.println("bulkIndex counter:" + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
    }
}
