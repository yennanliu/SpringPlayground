package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/** book p.104 */

public interface BookDao extends JpaRepository<Book, Integer> {

    @Query(value="SELECT * FROM t_book WHERE t_book.name like %?1% ", nativeQuery = true)
    public List<Book> findByName(String name);

    @Query(value="SELECT * FROM t_book ORDER BY RAND() LIMIT ?1 ", nativeQuery = true)
    public List<Book> randomList(Integer id);

}
