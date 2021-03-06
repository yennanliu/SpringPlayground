package np.com.roshanadhikary.mdblog.mapper;

import np.com.roshanadhikary.mdblog.entities.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostMapper {

    @Select("SELECT * FROM posts where id = #{id}")
    public Post getById(long id);

    // TODO : fix author_id parse (getter maybe ?) so can remove hardcode here
    //@Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `dateTime`) values(#{id}, #{title},  #{content}, #{synopsis}, #{author_id}, #{dateTime})")
    @Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `date_time`) values(#{id}, #{title},  #{content}, #{synopsis}, 1, #{dateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertPost(Post post);

}
