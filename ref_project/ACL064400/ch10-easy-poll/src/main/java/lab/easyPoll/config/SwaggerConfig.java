package lab.easyPoll.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.builder.ApiInfoBuilder;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
public class SwaggerConfig {

	@Autowired
	private SpringSwaggerConfig springSwaggerConfig;

	@Bean
	public SwaggerSpringMvcPlugin createSwaggerSpringMvcPlugin() {

		SwaggerSpringMvcPlugin swaggerSpringMvcPlugin 
				= new SwaggerSpringMvcPlugin(this.springSwaggerConfig);

		configureSwagger(swaggerSpringMvcPlugin);

		return swaggerSpringMvcPlugin;
	}

	private void configureSwagger(SwaggerSpringMvcPlugin plugin) {
		ApiInfo apiInfo 
			= new ApiInfoBuilder()
					.title("EasyPoll REST API")
					.description("EasyPoll Api for creating and managing polls")
					.termsOfServiceUrl("http://example.com/terms-of-service")
					.contact("info@example.com")
					.license("MIT License")
					.licenseUrl("http://opensource.org/licenses/MIT")
					.build();

		plugin
			.apiInfo(apiInfo)
			.apiVersion("1.0")
			.includePatterns("/polls/*.*", "/votes/*.*", "/computeresult/*.*");

		plugin.useDefaultResponseMessages(true);
	}

}
