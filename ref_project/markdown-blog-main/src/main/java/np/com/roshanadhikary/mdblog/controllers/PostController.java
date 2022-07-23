package np.com.roshanadhikary.mdblog.controllers;

import np.com.roshanadhikary.mdblog.entities.Author;
import np.com.roshanadhikary.mdblog.entities.Post;
import np.com.roshanadhikary.mdblog.repositories.PostRepository;
import np.com.roshanadhikary.mdblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {
	private final PostRepository postRepository;
	private final int PAGINATIONSIZE = 100; // how many posts show in a http://localhost:8080/posts/ page

	@Autowired
	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Autowired
	PostService postService;

	@GetMapping("")
	public String getPaginatedPosts(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = "" + PAGINATIONSIZE) int size,
			Model model) {

		Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Post> postsPage = postRepository.findAll(pageRequest);
		List<Post> posts = postsPage.toList();

		long postCount = postRepository.count();
		int numOfPages = (int) Math.ceil((postCount * 1.0) / PAGINATIONSIZE);

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

	@ResponseBody
	@PostMapping("/create")
	public Post createPost(@RequestParam(value = "id") int id,
						   @RequestParam(value = "title") String title,
						   @RequestParam(value = "content") String content,
						   @RequestParam(value = "synopsis") String synopsis,
						   @RequestParam(value = "author_id") int author_id){

		System.out.println(">>>> create start ...");

		Post post = new Post();
		post.setId(id);
		post.setTitle(title);
		post.setSynopsis(synopsis);
		post.setContent(content);

		System.out.println(">>> post = " + post);

		// TODO : fix this
		Author author = new Author();
		author.setId(author_id);

		post.setAuthor(author);

		System.out.println(">>>> create end ...");

		postService.savePost(post);

		return post;
	}
}