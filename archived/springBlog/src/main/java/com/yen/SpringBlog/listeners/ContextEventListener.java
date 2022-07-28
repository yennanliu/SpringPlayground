package com.yen.SpringBlog.listeners;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-4.html

import com.yen.SpringBlog.entities.Author;
import com.yen.SpringBlog.entities.Post;
import com.yen.SpringBlog.repository.AuthorRepository;
import com.yen.SpringBlog.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yen.SpringBlog.utils.MdFileReader.*;
import static com.yen.SpringBlog.utils.PostUtil.*;
import static com.yen.SpringBlog.utils.AuthorUtil.*;

@Slf4j
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
            // https://stackoverflow.com/questions/14526260/how-do-i-get-the-file-name-from-a-string-containing-the-absolute-file-path
            Path p = Paths.get(postFile.toString());
            //Optional<String> postFileNameOpt = Optional.ofNullable(postFile.getFilename());
            Optional<String> postFileNameOpt = Optional.ofNullable(p.getFileName().toString());

            log.info(">>> p = {}", p);
            log.info(">>> postFileNameOpt = {}", postFileNameOpt);

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
                    log.info(">>> Post with ID: " + id + " does not exist. Creating post...");

                    post.setTitle(title);
                    post.setAuthor(author);
                    post.setContent(htmlContent);
                    post.setSynopsis(getSynopsisFromHtmlContent(htmlContent));
                    post.setLocalDateTime(LocalDateTime.now());

                    postRepository.save(post);

                    log.info(">>> Post with ID: " + id + " created.");
                } else {
                    log.info(">>> Post with ID: " + id + " exists.");
                }
            } else {
                log.info(">>> postFileName is null, should not be null");
            }
        });
    }

}
