package com.yen.SpringBlog.listeners;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-4.html

import com.yen.SpringBlog.entities.Author;
import com.yen.SpringBlog.entities.Post;
import com.yen.SpringBlog.repository.AuthorRepository;
import com.yen.SpringBlog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yen.SpringBlog.utils.MdFileReader.*;
import static com.yen.SpringBlog.utils.PostUtil.*;
import static com.yen.SpringBlog.utils.AuthorUtil.*;

public class ContextEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     *  With @Value, we inject into the postFiles,
     *  an array of Resource type, any files that exist in the classpath inside posts directory.
     *
     *  e.g. /src/main/resources/posts
     */
    @Value("classpath:posts/*")
    private Resource[] postFiles;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Arrays.stream(postFiles).forEach(postFile -> {
            // TODO : fix below
            //Optional<String> postFileNameOpt = Optional.ofNullable(postFile.getFilename());
            Optional<String> postFileNameOpt = Optional.ofNullable("xxx");
            Post post = new Post();

            if (postFileNameOpt.isPresent()) {

                String postFileName = postFileNameOpt.get();
                String title = getTitleFromFileName(postFileName);
                long id = getIdFromFileName(postFileName);

                List<String> mdLines = readLinesFromMdFile(postFileName);
                String htmlContent = getHtmlContentFromMdLines(mdLines);

                Author author = bootstrapAuthor(authorRepository);

                Optional<Post> postOpt = postRepository.findById(id);

                // TODO : check if below logic works
                if (postOpt == null) {
                    System.out.println("Post with ID: " + id + " does not exist. Creating post...");

                    post.setTitle(title);
                    post.setAuthor(author);
                    post.setContent(htmlContent);
                    post.setSynopsis(getSynopsisFromHtmlContent(htmlContent));
                    post.setLocalDateTime(LocalDateTime.now());

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
