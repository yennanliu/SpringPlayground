package com.yen.mdblog.entity.Po;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@ToString
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String url;

	@Column
	private Date createTime;

	@Column
	private Date updateTime;

	@Column
	//@OneToMany(mappedBy = "author_id")
	//private List<Post> posts;
	private String posts;
}