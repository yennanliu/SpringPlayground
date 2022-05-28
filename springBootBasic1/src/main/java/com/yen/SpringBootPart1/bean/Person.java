package com.yen.SpringBootPart1.bean;

// https://www.youtube.com/watch?v=s5GTuFsCSWw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=21

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@ConfigurationProperties(prefix="person") // via this, we can get Person value from application.properties or application.yml (with person prefix)
@ToString
@Data
public class Person {
    // attr
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet pet;
    private String[] interests;
    private List<String> animals;
    private Map<String, Object> score;
    private Set<Double> salary;
    private Map<String, List<Pet>> allPets;
 }
