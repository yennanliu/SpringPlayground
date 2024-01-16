package com.yen.FlinkRestService.model.dto.zeppelin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNoteDto {

    private String notePath; // notebook name
    private String InterpreterGroup; // interpreter, e.g. spark, flink, python.. // TODO : replace with enums
}
