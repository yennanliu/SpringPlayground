package np.com.roshanadhikary.mdblog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.roshanadhikary.mdblog.util.LocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
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

	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;

	@Column
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime dateTime;
}