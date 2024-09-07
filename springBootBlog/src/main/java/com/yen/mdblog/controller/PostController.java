package com.yen.mdblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.mdblog.entity.Dto.SearchRequest;
import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.entity.Vo.CreateComment;
import com.yen.mdblog.entity.Vo.CreatePost;
//import com.yen.mdblog.mapper.AuthorMapper;
import com.yen.mdblog.mapper.PostMapper;
import com.yen.mdblog.repository.AuthorRepository;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.service.AuthorService;
import com.yen.mdblog.service.CommentService;
import com.yen.mdblog.service.PostService;
import com.yen.mdblog.util.PostUtil;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO : refactor : move all main logic to service
@Controller
@RequestMapping("/posts")
@Log4j2
public class PostController {

  private final int PAGINATION_SIZE =
      3; // how many posts show in a http://localhost:8080/posts/ page
  @Autowired PostRepository postRepository;
  @Autowired PostService postService;

  @Autowired AuthorService authorService;

  @Autowired PostMapper postMapper;

  @Autowired CommentService commentService;

  //@Autowired AuthorMapper authorMapper;

  @Autowired
  AuthorRepository authorRepository;

  @GetMapping("/all")
  public String getPaginatedPosts(
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "0" + PAGINATION_SIZE) int pageSize,
      Principal principal,
      Model model) {

    // Use pageHelper : https://www.796t.com/article.php?id=200769
    Pageable pageRequest =
        PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "DateTime"));

    //Page<Post> postsPage = postRepository.findAll(pageRequest);
    Flux<Post> postsPage = postRepository.findAll();

    // TODO : double check below
    //List<Post> posts = postsPage.toList();
    List<Post> posts = postsPage.toStream().collect(Collectors.toList());

    log.info(">>> posts length = {}", posts.toArray().length);
    PageInfo<Post> pageInfo = null;
    // 為了程式的嚴謹性，判斷非空：
    if (pageNum <= 0) {
      pageNum = 0;
    }
    log.info("當前頁是：" + pageNum + "顯示條數是：" + pageSize);
    // 1.引入分頁外掛,pageNum是第幾頁，pageSize是每頁顯示多少條,預設查詢總數count
    PageHelper.startPage(pageNum, pageSize);
    // 2.緊跟的查詢就是一個分頁查詢-必須緊跟.後面的其他查詢不會被分頁，除非再次呼叫PageHelper.startPage
    try {
      // Page<Post> postList = postRepository.findAll(pageRequest);//service查詢所有的資料的介面
      List<Post> postList = postMapper.getAllPosts(); // service查詢所有的資料的介面
      log.info(">>> 分頁資料：" + postList.get(0).toString());
      // 3.使用PageInfo包裝查詢後的結果,5是連續顯示的條數,結果list型別是Page<E>
      pageInfo = new PageInfo<Post>(postList, pageSize);
      // 4.使用model/map/modelandview等帶回前端
      model.addAttribute("pageInfo", pageInfo);
    } finally {
      PageHelper.clearPage(); // 清理 ThreadLocal 儲存的分頁引數,保證執行緒安全
    }

    log.info(">>> all posts count = " + posts.size());
    model.addAttribute("posts", posts);
    // get current user login via spring security
    //model.addAttribute("user", principal.getName());
    Arrays.stream(pageInfo.getNavigatepageNums()).forEach(System.out::println);
    //model.addAttribute("user", principal.getName());

    return "post/posts";
  }

  @GetMapping("/{id}")
  public String getPostById(@PathVariable long id, Model model, Principal principal) {

    //Optional<Post> postOptional = postRepository.findById(id);
    Mono<Post> postOptional = postRepository.findById(id);
    Disposable unused = postOptional.flatMap(post -> {
      // if existed
      model.addAttribute("post", postOptional.blockOptional().get());
      model.addAttribute("comment", new CreateComment());
      // load comment
      List<Comment> commentList = commentService.getCommentsByPostId(id);
      // only add to model when comment size > 0
      return Mono.just(post);
    }).switchIfEmpty(Mono.fromRunnable(() -> {
      System.out.println("Post not found");
      model.addAttribute("error", "no-post");
    })).subscribe();


//    if (postOptional.isPresent()) {
//      model.addAttribute("post", postOptional.get());
//      model.addAttribute("comment", new CreateComment());
//      // load comment
//      List<Comment> commentList = commentService.getCommentsByPostId(id);
//      // only add to model when comment size > 0
//      if (commentList.size() > 0) {
//        model.addAttribute("comments", commentList);
//      }
//    } else {
//      model.addAttribute("error", "no-post");
//    }

    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "post/post";
  }

  @GetMapping("/create")
  public String createPostForm(Model model, Principal principal) {

    model.addAttribute("CreatePost", new CreatePost());
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "post/create_post";
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createPost(CreatePost request, Model model, Principal principal) {

    log.info(">>> create post start ...");
    Post post = new Post();
    Author author = new Author();
    //String authorName = principal.getName();
    String authorName = "admin";
    //List<Author> authors = authorService.getAllAuthors();
    Flux<Author> authors = authorService.getAllAuthors();

    // TODO : do below with ids instead
    //List<String> names = authors.stream().map(x -> x.getName()).collect(Collectors.toList());
    List<String> names = authors.toStream().map(x -> x.getName()).collect(Collectors.toList());

    Boolean authorExisted = names.contains(authorName);

    if (!authorExisted) {
      author.setCreateTime(new Date());
      author.setUpdateTime(new Date());
      author.setName(authorName);

      //authorMapper.insertAuthor(author);
      authorRepository.save(author);

      //Integer id = authorService.getByName(authorName).getId();
      Integer id = authorService.getByName(authorName).block().getId();

      author.setId(id);
    } else {
      //Integer id = authorService.getByName(authorName).getId();
      Integer id = authorService.getByName(authorName).block().getId();

      author.setId(id);
    }
    BeanUtils.copyProperties(request, post);
    post.setId(postService.getTotalPost() + 1);
    post.setDateTime(LocalDateTime.now());
    post.setSynopsis(PostUtil.getSynopsis(request.getContent()));
    post.setAuthorId(author.getId());

    post.setFontSize(request.getFontSize());
    post.setFontStyle(request.getFontStyle());
    post.setFontColor(request.getFontColor());

    post.setDateTime(LocalDateTime.now());
    log.info(">>> request = " + request + " post = " + post + " author = " + author);
    log.info(">>>> create post end ...");
    postService.savePost(post);
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "post/success";
  }

  @GetMapping("/mypost")
  public String getMyPost(
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "0" + PAGINATION_SIZE) int pageSize,
      Principal principal,
      Model model) {

    //Author author = authorService.getByName(principal.getName());
    Mono<Author> author = authorService.getByName(principal.getName());

    // if there is current user has no any post
    if (author == null) {
      //model.addAttribute("user", principal.getName());
      model.addAttribute("user", "admin");
      log.info("use null_my_post");
      return "post/null_my_post";
    }

    // filter posts with authorId
    //List<Post> posts = postService.getPostsById(author.getId());
    List<Post> posts = postService.getPostsById(author.block().getId());

    model.addAttribute("posts", posts);
    // get current user login via spring security
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "post/my_post";
  }

  @GetMapping("/pre_search")
  public String preSearchPost(Model model, Principal principal) {

    model.addAttribute("SearchRequest", new SearchRequest());
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    return "post/post_presearch";
  }

  @PostMapping("/search")
  public String searchPost(Model model, Principal principal, SearchRequest request) {

    List<Post> posts = postService.getPostByKeyword(request);
    //model.addAttribute("user", principal.getName());
    model.addAttribute("user", "admin");
    model.addAttribute("posts", posts);
    return "post/post_search_result";
  }
}
