package com.anji.elasticsearchgeo.controller;

import com.anji.elasticsearchgeo.model.Person;
import com.anji.elasticsearchgeo.service.PersonService;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description:
 * author: chenqiang
 * date: 2018/6/5 15:33
 */
@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/add")
    public Object add() {
        double lat = 39.929986;
        double lon = 116.395645;

        List<Person> personList = new ArrayList<>(900000);

        for (int i = 100000; i < 1000000; i++) {
            double max = 0.00001;
            double min = 0.000001;
            Random random = new Random();

            double s = random.nextDouble() % (max - min + 1) + max;
            DecimalFormat df = new DecimalFormat("######0.000000");

            String lons = df.format(s + lon);
            String lats = df.format(s + lat);
            Double dlon = Double.valueOf(lons);
            Double dlat = Double.valueOf(lats);

            Person person = new Person();
            person.setId(i);

            person.setName("名字" + i);
            person.setPhone("电话" + i);
            person.setAddress(dlat + "," + dlon);

            personList.add(person);
        }
        personService.bulkIndex(personList);

        return "添加数据";
    }

    @GetMapping("/query")
    public Object query() {
        double lat = 39.929986;
        double lon = 116.395645;

        Long nowTime = System.currentTimeMillis();
        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("address").point(lat, lon).distance(500, DistanceUnit.METERS);

//        GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("address")
//                .point(lat, lon).unit(DistanceUnit.METERS).order(SortOrder.ASC);


        Pageable pageable = new PageRequest(0, 50);

        NativeSearchQueryBuilder builder1 = new NativeSearchQueryBuilder().withFilter(builder).withPageable(pageable);

        SearchQuery searchQuery = builder1.build();

        List<Person> personList = elasticsearchTemplate.queryForList(searchQuery, Person.class);

        System.out.println("耗时：" + (System.currentTimeMillis() - nowTime));

        System.out.println(personList.size());

        for (Person person : personList) {
            System.out.println(person.getId() + "," + person.getName() + "," + person.getPhone() + "," + person.getAddress());
        }
        return personList;

    }
}
