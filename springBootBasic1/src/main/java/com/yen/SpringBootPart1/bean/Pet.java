package com.yen.SpringBootPart1.bean;

// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9
// https://www.youtube.com/watch?v=s5GTuFsCSWw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=21

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Pet {

    private String name;
    private Double weight;

    public Pet(){

    }

    public Pet(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                '}';
    }
}
