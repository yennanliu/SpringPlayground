package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class SqlJobSubmitDto {

    private String statement;

    @Override
    public String toString() {

        // "{\"statement\": \"SELECT 1, 2, 3\"}";
        return "{\"statement\": " + '"' + statement + '"' + "}";
    }

}
