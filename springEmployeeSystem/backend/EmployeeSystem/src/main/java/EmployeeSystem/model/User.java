package EmployeeSystem.model;

import EmployeeSystem.enums.Role;
//import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @Column(name = "password")
  private String password;

  @Column(name = "departement_id")
  private Integer departementId;

  @Column(name = "manager_id")
  private Integer managerId;

  @Lob
  @Column(name = "photo")
  // @Column(name = "photo", columnDefinition = "BLOB") // TODO : fix this
  private byte[] photo; // Binary data for storing the user photo

  public User(String firstName, String lastName, String email, Role role, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.role = role;
    this.password = password;
  }


  public User(String firstName, String lastName, String email, Role role, String password, byte[] photo) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.role = role;
    this.password = password;
    this.photo = photo;
  }

}
