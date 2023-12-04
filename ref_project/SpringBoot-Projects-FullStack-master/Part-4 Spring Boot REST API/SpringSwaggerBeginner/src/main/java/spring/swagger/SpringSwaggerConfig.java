package spring.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.base.Predicates.or;

<<<<<<< HEAD
import com.google.common.base.Predicate;

import static com.google.common.base.Predicates.or;
=======
import java.util.function.Predicate;
>>>>>>> a608fe36a37e7194bb4a3524350efcd6110d82ea
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @Created 03 / 04 / 2020 - 3:23 PM
 * @project SpringSwaggerBeginner
 * @Author Hamdamboy
 */

@Profile("swagger-enable-for-qa")
@Configuration
@EnableSwagger2
public class SpringSwaggerConfig {
    //
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

<<<<<<< HEAD


    private Predicate<String> postPaths() {
        return or(regex("/api/posts.*"), regex("/api/javainuse.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Aspera API using FastFor")
                .description("Aspera API reference for developers")
=======
    private Predicate postPaths() {
        return (Predicate) or(regex("/api/posts.*"), regex("/api/javainuse.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("JavaInUse API")
                .description("JavaInUse API reference for developers")
>>>>>>> a608fe36a37e7194bb4a3524350efcd6110d82ea
                .termsOfServiceUrl("http://javainuse.com")
                .contact("javainuse@gmail.com").license("JavaInUse License")
                .licenseUrl("javainuse@gmail.com").version("1.0").build();
    }

}
