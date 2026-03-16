package com.yen.FlinkRestService.model.dto.zeppelin;

import com.yen.FlinkRestService.model.enums.InterpreterGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNoteDto {

    @NotBlank(message = "Note path is required")
    private String notePath;

    private InterpreterGroup interpreterGroup;

    /**
     * Returns the interpreter group value for Zeppelin API.
     */
    public String getInterpreterGroupValue() {
        return interpreterGroup != null ? interpreterGroup.getValue() : null;
    }
}
