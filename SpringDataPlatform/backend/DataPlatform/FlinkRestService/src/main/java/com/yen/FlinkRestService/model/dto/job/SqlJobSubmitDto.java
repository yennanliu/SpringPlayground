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

    // "{\"statement\": \"SELECT 1, 2, 3\"}";
    @Override
    public String toString() {
        return "{\"statement\": " + '"' + statement + '"' + "}";
    }
}
