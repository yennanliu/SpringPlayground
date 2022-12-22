package com.yen.SpringMapStruct.entity.vo;

// https://www.tpisoftware.com/tpu/articleDetails/2443

import com.yen.SpringMapStruct.entity.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(value = AccessLevel.NONE)
@Builder(setterPrefix = "set", toBuilder = true)
public class PersonVO {
    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;
}