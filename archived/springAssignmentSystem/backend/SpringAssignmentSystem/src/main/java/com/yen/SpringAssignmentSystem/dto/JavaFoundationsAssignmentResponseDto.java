package com.yen.SpringAssignmentSystem.dto;


import com.yen.SpringAssignmentSystem.domain.Assignment;
import com.yen.SpringAssignmentSystem.enums.AssignmentEnum;
import com.yen.SpringAssignmentSystem.enums.AssignmentStatusEnum;
import com.yen.SpringAssignmentSystem.enums.JavaFoundationsAssignmentEnum;

public class JavaFoundationsAssignmentResponseDto implements AssignmentResponseDto {
    private Assignment assignment;
    private AssignmentStatusEnum[] statusEnums = AssignmentStatusEnum.values();
    private JavaFoundationsAssignmentEnum[] javaFoundationsAssignmentEnums = JavaFoundationsAssignmentEnum.values();

    public JavaFoundationsAssignmentResponseDto(Assignment assignment) {
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
        return javaFoundationsAssignmentEnums;
    }

    public AssignmentStatusEnum[] getStatusEnums() {
        return statusEnums;
    }

}
