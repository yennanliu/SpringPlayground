package lab.easyPoll.config;

import static com.google.common.base.Predicates.or;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("lab.easyPoll.controller"))
				// .apis(RequestHandlerSelectors.any())
				.paths(or(PathSelectors.regex("/polls/*.*"), PathSelectors.regex("/computeresult/*.*")))
				// .paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("EasyPoll REST API")
				.description("EasyPoll Api for creating and managing polls")
				.termsOfServiceUrl("http://example.com/terms-of-service")
				.contact(new Contact("Jim", "http://somewhere", "info@example.com"))
				.license("MIT License")
				.licenseUrl("http://opensource.org/licenses/MIT")
				.version("1.0")
				.build();
	}
}
