package EmployeeSystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VacationDto {

    //private String period; // e.g. 20230101-20230102

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer userId;

    private String type;

    private String status;
}
