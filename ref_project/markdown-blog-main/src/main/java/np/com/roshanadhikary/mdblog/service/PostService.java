package np.com.roshanadhikary.mdblog.service;

import np.com.roshanadhikary.mdblog.entities.Post;

public interface PostService {

    Post getById(Long id);

    void savePost(Post post);

}
