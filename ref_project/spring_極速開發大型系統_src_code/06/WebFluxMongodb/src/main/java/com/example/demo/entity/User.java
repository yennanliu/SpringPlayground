package com.example.demo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author longzhonghua
 * @data 2/20/2019 1:25 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
//    為name建立索引,若果需要值唯一，則設定@Indexed(unique = true)。
    private String name;
    private int age;

}
