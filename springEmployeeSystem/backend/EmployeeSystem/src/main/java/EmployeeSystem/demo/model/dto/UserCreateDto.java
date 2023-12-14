package EmployeeSystem.demo.model.dto;

import EmployeeSystem.demo.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreateDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private Role role;

    private String email;

    private String password;
}
