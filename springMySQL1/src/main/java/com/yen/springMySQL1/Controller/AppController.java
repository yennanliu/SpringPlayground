package com.yen.springMySQL1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yen.springMySQL1.model.Product;
import com.yen.springMySQL1.Service.ProductService;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private ProductService service;

    // method
    @RequestMapping("/")
    public String viewHomePage(Model model){
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);

        return "index";
    }

    @RequestMapping("/new")
    public String showNewProductPage(Model model){
        Product product = new Product();
        model.addAttribute("product", product);

        return "new_product";
    }
}
