package np.com.roshanadhikary.mdblog.controllers;

import np.com.roshanadhikary.mdblog.entities.request.CreatePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/new")
public class CreateController {

    @GetMapping("")
    public String index(@RequestBody CreatePost request) {
        // https://ithelp.ithome.com.tw/articles/10193631
        System.out.println(">>> request = " + request);
        return "create_post";
    }
}