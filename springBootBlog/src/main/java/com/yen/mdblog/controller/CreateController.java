//package np.com.roshanadhikary.mdblog.controllers;
//
//import np.com.roshanadhikary.mdblog.entities.Post;
//import np.com.roshanadhikary.mdblog.entities.User;
//import np.com.roshanadhikary.mdblog.entities.request.CreatePost;
//import np.com.roshanadhikary.mdblog.service.PostService;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@Controller
//public class CreateController {
//
//    @Autowired
//    PostService postService;
//
//    @GetMapping("/new")
//    public String addPost(ModelMap map) {
//
//        System.out.println(">>> map = " + map);
//
//        map.put("CreatePost", map);
//
//        System.out.println(">>> map = " + map);
//
//        Post post = new Post();
//        BeanUtils.copyProperties(map, post);
//
//        System.out.println(">>> post = " + post);
//
//        postService.savePost(post);
//
//        // https://ithelp.ithome.com.tw/articles/10193631
//        return "create_post";
//    }
//
//
//    @ResponseBody
//    @RequestMapping(value="/result", method= RequestMethod.POST)
//    public String add(@ModelAttribute CreatePost createPost){
//
//        String title = createPost.getTitle();
//        Long id = createPost.getId();
//
//        return "result : " + title +" __ " + id;
//    }
//
//}