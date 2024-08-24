package com.yen.webFluxPoc;

// https://youtu.be/anguDoWURus?si=soqM6UrLcqB3td_i&t=580

// import com.jayway.jsonpath.Criteria;
import com.yen.webFluxPoc.model.Author;
import com.yen.webFluxPoc.repository.AuthorRepository;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;

// import io.asyncer.r2dbc.mysql.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.FetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

// https://youtu.be/hGgf-rTpvJ8?si=I95pXhaxKxKlyKKF&t=604
@SpringBootTest
public class R2DBCTest {

  // spring data module test

  /**
   * option 1 : R2dbcEntityTemplate
   *
   * <p>1. not good for join query (single table query) 2. CRUD API
   */
  @Autowired R2dbcEntityTemplate r2dbcEntityTemplate; // CRUD API

  /**
   * option 2 : DatabaseClient (BETTER !!!)
   *
   * <p>1. low level API 2. can do complex query op (e.g. join ..)
   */
  @Autowired DatabaseClient databaseClient; // DB client (can get DB conn directly)

  /** option 3 : use repository (similar as mybatis plus */
  @Autowired
  AuthorRepository authorRepository;

  @Test
  public void test1() {

    /** r2dbc is a async DB connection library 2) CAN NOT raise concurrent amount */

    // Step 0) mysql setting
    final String mysqlUrl = "r2dbc:mysql://localhost:3306/webflux_test";
    // ConnectionFactory connectionFactory = ConnectionFactories.get(mysqlUrl);
    // https://youtu.be/anguDoWURus?si=kKvC_BpiWANXqdtT&t=2323
    MySqlConnectionConfiguration conf =
        MySqlConnectionConfiguration.builder()
            .username("root")
            .host("localhost")
            .database("webflux_test")
            .build();

    // Step 1) mysql connection factory
    MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(conf);

    // Step 2) get connection, send SQL

    // Step 3) publish received data
    // https://youtu.be/anguDoWURus?si=pyqt50AvCmoezet6&t=1327
    Mono.from(connectionFactory.create())
        .flatMapMany(
            connection ->
                Flux.from(
                    connection
                        .createStatement("SELECT * FROM author WHERE id = ?")
                        .bind(0, 2)
                        .execute()))
        .map(
            result -> {
              return Mono.from(
                  result.map(
                      readable -> {
                          Integer id = readable.get("id", Integer.class);
                        String name = readable.get("name", String.class);
                        return new Author(id, name);
                      }));
            })
        .subscribe(
            authorMono -> {
              System.out.println(authorMono);
            });
  }

  /** R2dbcEntityTemplate test */
  // https://youtu.be/_1HwzpWx5UM?si=GGMfn3s_0uoN2Eq5&t=156
  // https://youtu.be/hGgf-rTpvJ8?si=7joaGY4QaE3KoREg&t=22
  @Test
  public void test2() throws InterruptedException {

    // Query by criteria (QBC)
    // step 1) prepare query condition
    Criteria criteria = Criteria.empty().and("id").is(1).and("name").is("jack");

    // step 2) prepare SQL
    Query query = Query.query(criteria);

    // step 3) run SQL
    r2dbcEntityTemplate
        .select(query, Author.class)
        .subscribe(author -> System.out.println(">>> author = " + author));

    Thread.sleep(20000);
  }

  /** DatabaseClient test (preferable !!!) */
  @Test
  public void test3() throws InterruptedException {

    /**
     * public interface RowsFetchSpec<T> { Mono<T> one();
     *
     * <p>Mono<T> first();
     *
     * <p>Flux<T> all(); }
     */
    String sql = "SELECT * FROM author WHERE id = ?";
    //      FetchSpec<Map<String, Object>> res = databaseClient.sql(sql)
    //              .bind(0, 1)
    //              .fetch(); // get data, can get one or first, or all (RowsFetchSpec interface)
    //
    //      Flux<Map<String, Object>> data = res.all();
    //
    //      data.subscribe(x->System.out.println(">>> data = " + data));

    databaseClient
        .sql(sql)
        .bind(0, 1)
        .fetch()
        .all()
        .map(
            map -> {
              System.out.println(">>> map = " + map);
              Integer id = (Integer) map.get("id");
              String name = map.get("name").toString();
              return new Author(id+10, name);
            })
        .subscribe(author -> System.out.println(">>> author = " + author));

    Thread.sleep(20000);
  }

    /** Repository test (preferable !!!) */
  // https://youtu.be/42MTtF44XAs?si=W1sTgqE3UeCqENNd&t=332
  @Test
  public void test4() throws InterruptedException {

      // example 1
      authorRepository.findAll()
              .subscribe(author -> System.out.println(">>> author = " + author));

      // example 2 : run with custom SQL
//      authorRepository.findAllByIdAndNameLike(Arrays.asList(1,2), "jack")
//              .subscribe(author -> System.out.println(">>> filter author = " + author));

      Thread.sleep(20000);
  }

}
