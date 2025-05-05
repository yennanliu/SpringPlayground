package EmployeeSystem.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import javax.persistence.*;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//@Entity
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

  //@Column(name = "start_date")
  private LocalDate startDate;

  //@Column(name = "end_date")
  private LocalDate endDate;

  //@Column(name = "user_id")
  private Integer userId;

  //@Column(name = "type")
  private String type;

  //@Column(name = "status")
  private String status;
}
