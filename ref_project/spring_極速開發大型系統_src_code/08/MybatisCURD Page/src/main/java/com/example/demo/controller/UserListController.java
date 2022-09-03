package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author longzhonghua
 * @data 2/22/2019 8:56 PM
 */
@Controller
public class UserListController {
    @Autowired
    UserMapper userMapper;
    @RequestMapping("/listall")
    public String listCategory(Model m, @RequestParam(value="start",defaultValue="0")int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        //1. 在參數裡接受目前是第幾頁 start ，以及每頁顯示多少條資料 size。 預設值分別是0和5。
        //2. 根據start,size進行分頁，並且設定id 倒排序
        PageHelper.startPage(start,size,"id desc");
        //3. 因為PageHelper的作用，這裡就會傳回目前分頁的集合了
        List<User> cs = userMapper.queryAll();
        //4. 根據傳回的集合，建立PageInfo物件
        PageInfo<User> page = new PageInfo<>(cs);
        //5. 把PageInfo物件扔進model，以供後續顯示
        m.addAttribute("page", page);
   //System.out.println(page..is.end.e.isIsLastPage().I.getLastPage().f.isIsFirstPage().getFirstPage().getLastPage().isIsFirstPage());
        //6. 跳躍到listCategory.jsp
        return "list";
    }
}
