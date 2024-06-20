package EmployeeSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "option_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OptionSchema {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "column_name")
  private String columnName;

  @Column(name = "schema_name")
  private String schemaName;

  @Column(name = "active")
  private Boolean active;
}
