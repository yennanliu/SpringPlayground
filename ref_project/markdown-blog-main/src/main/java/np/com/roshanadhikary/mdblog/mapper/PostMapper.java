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

    @Insert("INSERT INTO posts(`id`,`title`,`content`,`synopsis`,`author_id`, `dateTime`) values(#{id}, #{title},  #{content}, #{synopsis}, #{author_id}, #{dateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertPost(Post post);

}
