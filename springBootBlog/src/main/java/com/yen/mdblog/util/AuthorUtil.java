package com.yen.mdblog.util;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.repository.AuthorRepository;
import java.util.Date;
import java.util.Optional;

public class AuthorUtil {
	public static Author bootstrapAuthor(AuthorRepository authorRepository) {
		Optional<Author> authorOpt = authorRepository.findById(1L);
		if (authorOpt.isPresent()) {
			return authorOpt.get();
		} else {
			Author newAuthor = new Author();
			newAuthor.setName("yen");
			newAuthor.setEmail("y999@gmail.com");
			newAuthor.setUrl("yen.xx.yy");
			newAuthor.setCreateTime(new Date());
			newAuthor.setUpdateTime(new Date());
			authorRepository.save(newAuthor);
			return newAuthor;
		}
	}

}
