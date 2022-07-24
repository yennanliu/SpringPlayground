package np.com.roshanadhikary.mdblog.controllers;

import np.com.roshanadhikary.mdblog.entities.Post;
import np.com.roshanadhikary.mdblog.entities.request.CreatePost;
import np.com.roshanadhikary.mdblog.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/new")
public class CreateController {

    @Autowired
    PostService postService;

    @GetMapping("")
    public String addPost(@RequestParam Map<String, Object> request) {

        System.out.println(">>> request = " + request);

        Post post = new Post();
        BeanUtils.copyProperties(request, post);

        System.out.println(">>> post = " + post);

        postService.savePost(post);

        // https://ithelp.ithome.com.tw/articles/10193631
        return "create_post";
    }

}