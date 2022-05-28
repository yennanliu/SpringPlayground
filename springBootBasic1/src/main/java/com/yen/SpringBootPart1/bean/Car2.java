package com.yen.SpringBootPart1.bean;

// https://www.youtube.com/watch?v=0zZVSPVdtI0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=17

/**
 *  Set up Bean via lombok
 *  (simplify process : getter, setter, constructor, ToString, HashCode...)
 */

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j // add logger via lombok
@EqualsAndHashCode // add "equals" hashCode via lombok
@NoArgsConstructor // add constructors (without param) via lombok
@AllArgsConstructor // add all constructors (with all param) via lombok
@ToString // add ToString via lombok
@Data // add getter, setter via lombok
@Component
@ConfigurationProperties(prefix="mycar2")
public class Car2 {

    private String brand;
    private Integer price;

    public String test(){
        return "123";
    }

}
