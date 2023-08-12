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

			Author author1 = new Author();
			author1.setName("yen");
			author1.setEmail("y999@gmail.com");
			author1.setUrl("yen.xx.yy");
			author1.setCreateTime(new Date());
			author1.setUpdateTime(new Date());
			authorRepository.save(author1);

			return author1;
		}
	}

}
