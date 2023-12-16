package EmployeeSystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VacationDto {

    private String period; // e.g. 20230101-20230102

    private Integer userId;

    private String type;

    private String status;
}
