package com.yen.mdblog.listeners;

import com.yen.mdblog.entities.Post;
import com.yen.mdblog.repositories.AuthorRepository;
import com.yen.mdblog.repositories.PostRepository;
import com.yen.mdblog.util.AuthorUtil;
import com.yen.mdblog.util.MdFileReader;
import com.yen.mdblog.entities.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yen.mdblog.util.PostUtil.*;

@Component
public class ContextEventListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PostRepository postRepository;

	@Value("classpath:posts/*")
	private Resource[] postFiles;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Arrays.stream(postFiles).forEach(postFile -> {

			Optional<String> postFileNameOpt = Optional.ofNullable(postFile.getFilename());
			Post post = new Post();

			if (postFileNameOpt.isPresent()) {
				String postFileName = postFileNameOpt.get();
				String title = MdFileReader.getTitleFromFileName(postFileName);
				long id = MdFileReader.getIdFromFileName(postFileName);

				List<String> mdLines = MdFileReader.readLinesFromMdFile(postFileName);
				String htmlContent = getHtmlContentFromMdLines(mdLines);

				Author author = AuthorUtil.bootstrapAuthor(authorRepository);

				Optional<Post> postOpt = postRepository.findById(id);
				if (postOpt.isEmpty()) {
					System.out.println("Post with ID: " + id + " does not exist. Creating post...");
					post.setTitle(title);
					post.setAuthor(author);
					post.setContent(htmlContent);
					post.setSynopsis(getSynopsisFromHtmlContent(htmlContent));
					post.setDateTime(LocalDateTime.now());

					postRepository.save(post);
					System.out.println("Post with ID: " + id + " created.");
				} else {
					System.out.println("Post with ID: " + id + " exists.");
				}
			} else {
				System.out.println("postFileName is null, should not be null");
			}
		});
	}
}

