package com.yen.mdblog.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthor {

  private Integer id;
  private String name;
  private String email;
}
