package EmployeeSystem.model;

import EmployeeSystem.enums.VacationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

//    @Column(name = "period")
//    private String period; // e.g. 20230101-20230102

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;
}
