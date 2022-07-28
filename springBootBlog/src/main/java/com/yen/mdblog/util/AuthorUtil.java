package com.yen.mdblog.util;

import com.yen.mdblog.entities.Author;
import com.yen.mdblog.repositories.AuthorRepository;

import java.util.Optional;

public class AuthorUtil {
	public static Author bootstrapAuthor(AuthorRepository authorRepository) {
		Optional<Author> authorOpt = authorRepository.findById(1L);
		if (authorOpt.isPresent()) {
			return authorOpt.get();
		} else {

			Author roshanAuthor = new Author();
			roshanAuthor.setName("yen");
			roshanAuthor.setEmail("y999@gmail.com");
			roshanAuthor.setUrl("yen.xx.yy");

			authorRepository.save(roshanAuthor);
			return roshanAuthor;
		}
	}
}
