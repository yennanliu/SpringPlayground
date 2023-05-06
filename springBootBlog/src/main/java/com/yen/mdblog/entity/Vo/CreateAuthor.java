package com.yen.mdblog.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthor {

    private long id;
    private String name;
    private String email;
    private byte[] profilePic;
}
