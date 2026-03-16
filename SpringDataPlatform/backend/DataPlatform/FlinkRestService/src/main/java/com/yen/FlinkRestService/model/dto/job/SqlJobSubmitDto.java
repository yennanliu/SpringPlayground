package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlJobSubmitDto {

    @NotBlank(message = "SQL statement is required")
    private String statement;

    /**
     * Returns JSON format for Flink SQL Gateway API
     */
    public String toJsonPayload() {
        return "{\"statement\": \"" + statement + "\"}";
    }

    @Override
    public String toString() {
        return toJsonPayload();
    }
}
