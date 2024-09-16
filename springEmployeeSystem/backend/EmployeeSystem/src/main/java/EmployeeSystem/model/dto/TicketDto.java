package EmployeeSystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketDto {

  /**
   *  TODO : validate below:
   *
   *  The reason you’re seeing an UPDATE SQL statement being executed
   *  is because of the behavior of the R2dbcRepository.save() method.
   *  It performs either an INSERT or an UPDATE operation
   *  depending on whether the entity being saved has an ID value.
   *  If the ID is non-null, Spring Data assumes it’s an existing entity
   *  and issues an UPDATE. If it’s null, it issues an INSERT.
   */
  private Integer id;

  private String subject;
  private String description;
  private Integer userId;
  private Integer assignedTo; // assigned user id
  private String status;
  private String tag;
}
