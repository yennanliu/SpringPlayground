package com.yen.SpringAssignmentSystem.dto;

import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.enums.AssignmentEnum;
import com.yen.SpringAssignmentSystem.enums.AssignmentStatusEnum;
import com.yen.SpringAssignmentSystem.enums.BootcampAssignmentEnum;

public class BootcampAssignmentResponseDto implements AssignmentResponseDto {

    private Assignment assignment;
    private BootcampAssignmentEnum[] bootcampAssignmentEnums = BootcampAssignmentEnum.values();
    private AssignmentStatusEnum[] statusEnums = AssignmentStatusEnum.values();

    public BootcampAssignmentResponseDto() {
    }

    public BootcampAssignmentResponseDto(Assignment assignment) {
        super();
        this.assignment = assignment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public AssignmentEnum[] getAssignmentEnums() {
        return bootcampAssignmentEnums;
    }

    public AssignmentStatusEnum[] getStatusEnums() {
        return statusEnums;
    }
}
