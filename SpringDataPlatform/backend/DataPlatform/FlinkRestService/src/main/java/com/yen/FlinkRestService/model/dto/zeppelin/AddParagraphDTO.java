package com.yen.FlinkRestService.model.dto.zeppelin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddParagraphDTO {

    private String noteId;
    private String title;
    private String text;
}
