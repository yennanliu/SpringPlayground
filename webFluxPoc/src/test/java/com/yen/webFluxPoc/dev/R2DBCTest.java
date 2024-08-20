//package com.yen.webFluxPoc.dev;
//
//// https://youtu.be/anguDoWURus?si=soqM6UrLcqB3td_i&t=580
//
//import com.yen.webFluxPoc.model.Author;
//import io.r2dbc.spi.ConnectionFactories;
//import io.r2dbc.spi.ConnectionFactory;
//import org.junit.jupiter.api.Test;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//public class R2DBCTest {
//
//  @Test
//  public void test1() {
//
//    /**  r2dbc is a async DB connection library 2) CAN NOT raise concurrent amount */
//
//    // Step 1) mysql connection factory
//    final String mysqlUrl = "r2dbc:mysql://localhost:3306/test";
//    ConnectionFactory connectionFactory = ConnectionFactories.get(mysqlUrl);
//
//    // Step 2) get connection, send SQL
//
//    // Step 3) publish received data
//    // https://youtu.be/anguDoWURus?si=pyqt50AvCmoezet6&t=1327
//    Mono.from(connectionFactory.create())
//        .flatMapMany(
//            connection ->
//                Flux.from(
//                    connection
//                        .createStatement("SELECT * FROM author WHERE id = ?")
//                        .bind(0, 2L)
//                        .execute()))
//        .map(
//            result -> {
//              return Mono.from(
//                  result.map(
//                      readable -> {
//                        Long id = readable.get("id", Long.class);
//                        String name = readable.get("name", String.class);
//                        return new Author(id, name);
//                      }));
//            })
//        .subscribe(
//            authorMono -> {
//              System.out.println(authorMono);
//            });
//  }
//}
