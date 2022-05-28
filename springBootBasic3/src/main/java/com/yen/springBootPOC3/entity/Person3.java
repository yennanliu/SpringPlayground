package com.yen.springBootPOC3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/** book p.89 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person3 {

    @Id
    private String id;
    private String firstName;
    private String lastName;
}
