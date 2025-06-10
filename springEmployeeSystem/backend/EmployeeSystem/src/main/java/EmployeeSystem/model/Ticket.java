package EmployeeSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


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
   * 
   * Fixed to work with both MySQL 5 and MySQL 8 for auto table creation.
   */
  @Column(name = "description", columnDefinition = "LONGTEXT")
  private String description; // Use LONGTEXT type for longer strings

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "assigned_to")
  private Integer assignedTo; // assigned user id

  @Column(name = "status")
  private String status;

  @Column(name = "tag")
  private String tag;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
