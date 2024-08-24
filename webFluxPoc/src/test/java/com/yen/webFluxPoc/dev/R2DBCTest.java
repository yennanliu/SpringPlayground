package com.yen.webFluxPoc.dev;

// https://youtu.be/anguDoWURus?si=soqM6UrLcqB3td_i&t=580

import com.yen.webFluxPoc.model.Author;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class R2DBCTest {

  @Test
  public void test1() {

    /** r2dbc is a async DB connection library 2) CAN NOT raise concurrent amount */

    // Step 0) mysql setting
    final String mysqlUrl = "r2dbc:mysql://localhost:3306/test";
    // ConnectionFactory connectionFactory = ConnectionFactories.get(mysqlUrl);
    // https://youtu.be/anguDoWURus?si=kKvC_BpiWANXqdtT&t=2323
    MySqlConnectionConfiguration conf =
        MySqlConnectionConfiguration.builder()
            .username("root")
            .host("localhost")
            .database("test")
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
  @Test
  public void test2() {

  }

}
