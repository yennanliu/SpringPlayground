package com.yen.SwaggerDemo.config;

// https://www.gushiciku.cn/pl/pft6/zh-tw
// https://github.com/niumoo/springboot/blob/master/springboot-web-swagger/src/main/java/net/codingme/boot/config/SwaggerConfig.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestAPi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yen.SwaggerDemo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SwaggerDemo API")
                .description("com.yen.SwaggerDemo project")
                .termsOfServiceUrl("google.com")
                .version("1.0")
                .build();
    }

}
