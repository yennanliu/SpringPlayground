package EmployeeSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  //@Column(name = "name")
  private String name;

  //@Column(name = "users")
  @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "departement_id", referencedColumnName = "id")
  private Set<User> users = new HashSet<>();
}
