package com.yen.mdblog.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@ToString
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private String id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String url;

	@Column
	//@OneToMany(mappedBy = "author")
	private List<Post> posts;
}