package com.yen.mdblog.entity;

import com.yen.mdblog.util.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column
	private long id;

	@Column
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(length = 150)
	private String synopsis;

	@Column(length = 150)
	//@ManyToOne
	@JoinColumn(name = "author_id")
	//private Author author;
	private String authorId;

	@Column
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime dateTime;
}