package EmployeeSystem.model;

import java.util.Date;
//import javax.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "check_in")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Checkin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "create_time")
  private Date createTime;
}
