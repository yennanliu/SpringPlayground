package com.yen.webFluxPoc.dev;

// https://youtu.be/anguDoWURus?si=soqM6UrLcqB3td_i&t=580

// import com.jayway.jsonpath.Criteria;
import com.yen.webFluxPoc.model.Author;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;

// import io.asyncer.r2dbc.mysql.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class R2DBCTest {

  // spring data module test
  @Autowired R2dbcEntityTemplate r2dbcEntityTemplate; // CRUD API

  @Autowired DatabaseClient databaseClient; // DB client (can get DB conn directly)

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

  // https://youtu.be/_1HwzpWx5UM?si=GGMfn3s_0uoN2Eq5&t=156
  // https://youtu.be/hGgf-rTpvJ8?si=7joaGY4QaE3KoREg&t=22
  //  r2dbcEntityTemplate test
  @Test
  public void test2() throws InterruptedException {

    // Query by criteria (QBC)
    // step 1) prepare query condition
    Criteria criteria = Criteria.empty().and("id").is(1).and("name").is("jack");

    // step 2) prepare SQL
    Query query = Query.query(criteria);

    // step 3) run SQL
    r2dbcEntityTemplate.select(query, Author.class)
            .subscribe(author -> System.out.println(author));

    Thread.sleep(20000);
  }

  
}
