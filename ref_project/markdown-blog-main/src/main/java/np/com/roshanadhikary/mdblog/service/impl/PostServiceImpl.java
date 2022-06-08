package np.com.roshanadhikary.mdblog.service.impl;

import np.com.roshanadhikary.mdblog.entities.Post;
import np.com.roshanadhikary.mdblog.mapper.PostMapper;
import np.com.roshanadhikary.mdblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostMapper postMapper;

    @Override
    public Post getById(Long id) {
        return postMapper.getById(id);
    }

    @Override
    public void savePost(Post post) {
        postMapper.insertPost(post);
    }

}
