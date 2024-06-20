package EmployeeSystem.enums;

public enum TicketStatus {
  PENDING("PENDING"),
  CREATED("CREATED"),
  REVIEWING("REVIEWING"),
  APPROVED("APPROVED"),
  REJECTED("REJECTED"),
  CANCELLED("CANCELLED");

  // attr
  private String name;

  TicketStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
