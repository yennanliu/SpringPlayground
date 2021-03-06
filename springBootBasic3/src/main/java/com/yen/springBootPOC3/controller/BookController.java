package com.yen.springBootPOC3.controller;

import com.yen.springBootPOC3.dao.BookDao;
import com.yen.springBootPOC3.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.function.Predicate;

/** book p.105 */

@Controller
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookDao bookDao;

    // get all books
    @RequestMapping("/list")
    public ModelAndView list(){

        ModelAndView mav = new ModelAndView();
        System.out.println(">>>> bookDao.findAll() = " + bookDao.findAll().toString());

        mav.addObject("booklist", bookDao.findAll());
        mav.setViewName("bookList");
        return mav;
    }

    // add book
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Book book){
        bookDao.save(book);
        return "forward:/book/list";  // re-direct
    }

    // find book by id
    /** we have bookUpdate.html let user input updated book name, book author then back to  book/list */
    @GetMapping("/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("book", bookDao.getOne(id));
        mav.setViewName("bookUpDate");
        return mav;
    }

    // update book
    @PostMapping("/update")
    public String update(Book book){

        /** NOTE !!! update is called via POST in bookUpdate.html, then bookDao.save(book), and re-direct to book/list  */
        bookDao.save(book);

        return "forward:/book/list";
    }

    // delete book
    @GetMapping("/delete")
    public String deleter(Integer id){

        /** NOTE !!! delete is called via POST in bookList.html, then bookDao.deleteById(id), and re-direct to book/list */
        bookDao.deleteById(id);

        return "forward:/book/list";
    }

    // dynamic search by condition
    @GetMapping("/list2")
    public ModelAndView list2(Book book){
        ModelAndView mav = new ModelAndView();

        // TODO : fix below
        List<Book> bookList = bookDao.findAll();

        mav.addObject("book", book);
        mav.addObject("booklist", bookList);
        mav.setViewName("bookUpDate");

        return mav;
    }

    // query
    @ResponseBody
    @GetMapping("query")
    public List<Book> findByName(String name){
        return bookDao.findByName("python cookbook");
    }

    // random show
    @ResponseBody
    @GetMapping("/randomlist")
    public List<Book> randomList(String name){
        return bookDao.randomList(2);
    }

}
