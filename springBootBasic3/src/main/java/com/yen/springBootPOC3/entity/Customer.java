package com.yen.springBootPOC3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** book p.71 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Customer {

    // attr
    private long id;
    private String firstName;
    private String lastName;
}
