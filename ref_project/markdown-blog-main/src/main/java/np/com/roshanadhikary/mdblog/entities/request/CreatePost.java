package np.com.roshanadhikary.mdblog.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import np.com.roshanadhikary.mdblog.entities.Author;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePost {

    private long id;
    private String title;
    private String content;
    private String synopsis;
    private Author author;
    private LocalDateTime dateTime;
}
