package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class TestValidator {

    @GetMapping("/test")
    public String showForm(User user) {
        return "form";
    }

    @GetMapping("/results")
    public String results() {
        return "results";
    }

    @PostMapping("/test")
    public String checkUser(@Valid User user, BindingResult bindingResult, RedirectAttributes attr) {
        //特別注意實體中的屬性必須都驗證過了，不然不會成功
        if (bindingResult.hasErrors()) {
            return "form";
        }
        /**
         * @Description:
         * 1.使用RedirectAttributes的addAttribute方法傳遞參數會跟隨在URL後面 ，如上程式碼即為?name=long&age=45
         * 2.使用addFlashAttribute不會跟隨在URL後面，會把該參數值暫時儲存於session，待重新導向url取得該參數後從session中移除，
         * 這裡的redirect必須是方法映射路徑。你會發現redirect後的值只會出現一次，更新後不會出現了,對於重復傳送可以使用此來完成。
         */
        attr.addFlashAttribute("user", user);
        return "redirect:/results";

    }
}
 