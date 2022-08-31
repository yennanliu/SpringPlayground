package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Author;
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
import java.util.List;
import java.util.Optional;

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

	@GetMapping("/all")
	public String getPaginatedPosts(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = "" + PAGINATIONSIZE) int size,
			Model model) {

		Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "DateTime"));
		Page<Post> postsPage = postRepository.findAll(pageRequest);
		List<Post> posts = postsPage.toList();

		long postCount = postRepository.count();
		int numOfPages = (int) Math.ceil((postCount * 1.0) / PAGINATIONSIZE);

		log.info(">>> postCount = " + postCount);
		log.info(">>> numOfPages = " + numOfPages);

		model.addAttribute("posts", posts);
		model.addAttribute("postCount", postCount);
		model.addAttribute("pageRequested", page);
		model.addAttribute("paginationSize", PAGINATIONSIZE);
		model.addAttribute("numOfPages", numOfPages);

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
		author.setId(request.getId());

		int postCount = postService.getTotalPost();

		BeanUtils.copyProperties(request, post);
		post.setId(postCount+1);
		post.setDateTime(LocalDateTime.now());
		post.setSynopsis(request.getContent().substring(0, 10)); // get first 10 character as synopsis
		post.setAuthor(author);

		log.info(">>> request = " + request);
		log.info(">>> post = " + post);
		log.info(">>>> create post end ...");

		post.setDateTime(LocalDateTime.now());

		postService.savePost(post);
		authorService.saveAuthor(author);

		return "success";
	}

}