package com.yen.mdblog.listener;

import static com.yen.mdblog.util.PostUtil.*;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.entity.Po.Post;
import com.yen.mdblog.repository.AuthorRepository;
import com.yen.mdblog.repository.PostRepository;
import com.yen.mdblog.util.AuthorUtil;
import com.yen.mdblog.util.MdFileReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ContextEventListener implements ApplicationListener<ContextRefreshedEvent> {
  @Autowired private AuthorRepository authorRepository;

  @Autowired private PostRepository postRepository;

  @Value("classpath:posts/*")
  private Resource[] postFiles;

  // load all blog files in /resources/posts when app is launched
  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    Arrays.stream(postFiles)
        .forEach(
            postFile -> {
              Optional<String> postFileNameOpt = Optional.ofNullable(postFile.getFilename());
              Post post = new Post();

              if (postFileNameOpt.isPresent()) {
                String postFileName = postFileNameOpt.get();
                String title = MdFileReader.getTitleFromFileName(postFileName);
                long id = MdFileReader.getIdFromFileName(postFileName);
                List<String> mdLines = MdFileReader.readLinesFromMdFile(postFileName);
                String htmlContent = getHtmlContentFromMdLines(mdLines);
                //Author author = AuthorUtil.bootstrapAuthor(authorRepository);
                Mono<Author> author = AuthorUtil.bootstrapAuthor(authorRepository);
                //Optional<Post> postOpt = postRepository.findById(id);
                Mono<Post> postOpt = postRepository.findById(id);

                //				if (postOpt.isEmpty()) {
                //					System.out.println("Post with ID: " + id + " does not exist. Creating
                // post...");
                //					post.setTitle(title);
                //					post.setAuthor(author);
                //					post.setContent(htmlContent);
                //					post.setSynopsis(getSynopsisFromHtmlContent(htmlContent));
                //					post.setDateTime(LocalDateTime.now());
                //
                //					postRepository.save(post);
                //					System.out.println("Post with ID: " + id + " created.");
                //				} else {
                //					System.out.println("Post with ID: " + id + " exists.");
                //				}
                //			} else {
                //				System.out.println("postFileName is null, should not be null");

                try {
                  System.out.println("Post with ID: " + id + " does not exist. Creating post...");
                  post.setTitle(title);

                  // TODO : fix below
                  //post.setAuthorId(author.getId());
                  post.setAuthorId(author.block().getId());
                  // post.setAuthorId(author.getId());

                  post.setContent(htmlContent);
                  post.setSynopsis(getSynopsisFromHtmlContent(htmlContent));
                  post.setDateTime(LocalDateTime.now());

                  // TODO : avoid hardcode below
                  post.setFontSize("12px");
                  post.setFontStyle("Ariel");
                  post.setFontColor("black");

                  postRepository.save(post);
                } catch (Exception e) {
                  System.out.println(">>> create post failed : " + e);
                }
              }
            });
  }
}
