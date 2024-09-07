package EmployeeSystem.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import javax.persistence.*;

//import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


//@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  //@Column(name = "subject")
  private String subject;

  /**
   * Updated the @Column annotation for description to use columnDefinition = "TEXT".
   * This tells Hibernate to create the column as a TEXT type in the database,
   * which can hold much larger strings compared to a VARCHAR.
   *
   *
   *  NOTE !!!
   *    if app failed to start, comment out "@Column(name = "description", columnDefinition = "TEXT")"
   *    start app, then modify table attr type manually via below cmd :
   *
   *    ALTER TABLE tickets MODIFY COLUMN description TEXT;
   *
   *  -> then run app again
   */
  //@Column(name = "description")
  //@Column(name = "description", columnDefinition = "TEXT")
  private String description; // Use TEXT type for longer strings

  //@Column(name = "user_id")
  private Integer userId;

  //@Column(name = "assigned_to")
  private Integer assignedTo; // assigned user id

  //@Column(name = "status")
  private String status;

  //@Column(name = "tag")
  private String tag;
}
