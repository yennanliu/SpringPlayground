package com.yen.mdblog.entity.Po;

import com.yen.mdblog.util.DateTimeUtil;

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

	//@ManyToOne
	//@JoinColumn(name = "author_id")
	private Long authorId;
	//private Author author;

	@Column
	@Convert(converter = DateTimeUtil.class)
	private LocalDateTime dateTime;
}