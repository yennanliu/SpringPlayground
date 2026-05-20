package EmployeeSystem.model.dto;

import EmployeeSystem.enums.Role;

public interface UserSummary {
  Integer getId();
  String getFirstName();
  String getLastName();
  String getEmail();
  Role getRole();
  Integer getDepartementId();
  Integer getManagerId();
}
