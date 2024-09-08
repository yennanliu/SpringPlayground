package EmployeeSystem.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
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
  //@GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  //@Column(name = "name")
  private String name;

  // TODO : fix below
  //@Column(name = "users")
//  @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
//  @JoinColumn(name = "departement_id", referencedColumnName = "id")
//  private Set<User> users = new HashSet<>();

//  @org.springframework.data.annotation.Transient // To avoid mapping this field to the DB
//  private Set<User> users = new HashSet<>();

  // extra method help get user under department
  // Add a method to serialize the list into a JSON string
//  public void setUserList(List<String> userList) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    this.users = objectMapper.writeValueAsString(userList);
//    this.userList = userList;
//  }
//
//  // Deserialize the JSON string back into a list
//  public List<String> getUserList() throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    return objectMapper.readValue(this.users, List.class);
//  }

}
