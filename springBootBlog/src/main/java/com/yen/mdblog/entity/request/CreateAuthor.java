package com.yen.mdblog.entity.request;

import com.yen.mdblog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthor {

    private long id;
    private String name;
    private String email;
}
