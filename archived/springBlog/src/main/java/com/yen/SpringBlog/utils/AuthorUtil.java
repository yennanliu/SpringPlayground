package com.yen.SpringBlog.utils;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-4.html

import com.yen.SpringBlog.entities.Author;
import com.yen.SpringBlog.repository.AuthorRepository;

import java.util.Optional;

public class AuthorUtil {

    public static Author bootstrapAuthor(AuthorRepository authorRepository) {
        Optional<Author> authorOpt = authorRepository.findById(1L);

        if (authorOpt.isPresent()) {
            return authorOpt.get();
        }

        Author defaultAuthor = new Author();
        defaultAuthor.setName("yen");
        defaultAuthor.setName("yen@springBlog.com");
        defaultAuthor.setName("yen.name");

        authorRepository.save(defaultAuthor);

        return defaultAuthor;
    }

}
