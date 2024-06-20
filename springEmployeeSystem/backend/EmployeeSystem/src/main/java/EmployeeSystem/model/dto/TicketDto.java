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

  private Integer id;
  private String subject;
  private String description;
  private Integer userId;
  private Integer assignedTo; // assigned user id
  private String status;
  private String tag;
}
