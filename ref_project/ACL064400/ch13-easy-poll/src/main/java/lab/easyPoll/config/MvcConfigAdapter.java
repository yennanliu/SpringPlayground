package lab.easyPoll.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigAdapter implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		// Set the default size from 20 to 5
		resolver.setFallbackPageable(PageRequest.of(0, 5));
		argumentResolvers.add(resolver);
		WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
	}
}
