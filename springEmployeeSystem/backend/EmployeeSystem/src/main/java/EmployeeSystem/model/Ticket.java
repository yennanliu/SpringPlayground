package EmployeeSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "subject")
  private String subject;

  /**
   * Updated the @Column annotation for description to use columnDefinition = "TEXT".
   * This tells Hibernate to create the column as a TEXT type in the database,
   * which can hold much larger strings compared to a VARCHAR.
   */
  //@Column(name = "description", columnDefinition = "TEXT")
  @Column(name = "description")
  private String description; // Use TEXT type for longer strings

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "assigned_to")
  private Integer assignedTo; // assigned user id

  @Column(name = "status")
  private String status;

  @Column(name = "tag")
  private String tag;
}
