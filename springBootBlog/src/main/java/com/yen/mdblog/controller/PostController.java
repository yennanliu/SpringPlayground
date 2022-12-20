package com.yen.mdblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.mdblog.entity.Author;
import com.yen.mdblog.mapper.PostMapper;
import com.yen.mdblog.service.AuthorService;
import lombok.extern.log4j.Log4j2;
import com.yen.mdblog.entity.Post;
import com.yen.mdblog.entity.request.CreatePost;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@Log4j2
public class PostController {

	//private final PostRepository postRepository;

	@Autowired
	PostRepository postRepository;

	// TODO : implement paging with it
	private final int PAGINATIONSIZE = 3; // how many posts show in a http://localhost:8080/posts/ page

	@Autowired
	public PostController(PostRepository postRepository) {

		this.postRepository = postRepository;
	}

	@Autowired
	PostService postService;

	@Autowired
	AuthorService authorService;

	@Autowired
	PostMapper postMapper;

	@GetMapping("/all")
	public String getPaginatedPosts(
			@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
			@RequestParam(value="pageSize", defaultValue = "0" + PAGINATIONSIZE) int pageSize,
			Model model) {

		/**
		 *  Use pageHelper
		 *  	- https://www.796t.com/article.php?id=200769
		 */
		Pageable pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "DateTime"));
		Page<Post> postsPage = postRepository.findAll(pageRequest);
		List<Post> posts = postsPage.toList();

		log.info(">>> posts length = {}", posts.toArray().length);

		PageInfo<Post> pageInfo = null;

		//為了程式的嚴謹性，判斷非空：
		if(pageNum <= 0){
			pageNum = 0;
		}
		System.out.println("當前頁是："+pageNum+"顯示條數是："+pageSize);

		//1.引入分頁外掛,pageNum是第幾頁，pageSize是每頁顯示多少條,預設查詢總數count
		PageHelper.startPage(pageNum, pageSize);
		//2.緊跟的查詢就是一個分頁查詢-必須緊跟.後面的其他查詢不會被分頁，除非再次呼叫PageHelper.startPage
		try {
			//Page<Post> postList = postRepository.findAll(pageRequest);//service查詢所有的資料的介面
			List<Post> postList = postMapper.getAllPosts();//service查詢所有的資料的介面
			//System.out.println("分頁資料：" + posts);
			//3.使用PageInfo包裝查詢後的結果,5是連續顯示的條數,結果list型別是Page<E>
			pageInfo = new PageInfo<Post>(postList, pageSize);
			//4.使用model/map/modelandview等帶回前端
			model.addAttribute("pageInfo",pageInfo);
		}finally {
			PageHelper.clearPage(); //清理 ThreadLocal 儲存的分頁引數,保證執行緒安全
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("posts", posts);

		System.out.println(">>>>");
		Arrays.stream(pageInfo.getNavigatepageNums()).forEach(System.out::println);
		System.out.println(">>>>");

		//log.info(">>> model = {}", model);

		return "posts";
	}

	@GetMapping("/{id}")
	public String getPostById(@PathVariable long id, Model model) {
		Optional<Post> postOptional = postRepository.findById(id);

		if (postOptional.isPresent()) {
			model.addAttribute("post", postOptional.get());
		} else {
			model.addAttribute("error", "no-post");
		}

		return "post";
	}

	@GetMapping("/create")
	public String createPostForm(Model model){
		model.addAttribute("CreatePost", new CreatePost());
		return "create_post";
	}

	@RequestMapping(value="/create", method= RequestMethod.POST)
	public String createPost(CreatePost request){

		log.info(">>> create post start ...");

		Post post = new Post();
		Author author = new Author();
		Long authorId = request.getId();
		author.setId(authorId);

		int postCount = postService.getTotalPost();
		BeanUtils.copyProperties(request, post);
		post.setId(postCount+1);
		post.setDateTime(LocalDateTime.now());
		post.setSynopsis(request.getContent().substring(0, 10)); // get first 10 character as synopsis
		post.setAuthor(author);
		//post.setAuthorId(authorId);

		log.info(">>> request = " + request);
		log.info(">>> post = " + post);
		log.info(">>> author = " + author);
		log.info(">>>> create post end ...");

		post.setDateTime(LocalDateTime.now());

		postService.savePost(post);
		List<Author> authors = authorService.getAllAuthors();
		List<Long> ids = authors.stream().map(x -> x.getId()).collect(Collectors.toList());

		if (!ids.contains(author.getId())){
			authorService.saveAuthor(author);
		}

		return "success";
	}

}