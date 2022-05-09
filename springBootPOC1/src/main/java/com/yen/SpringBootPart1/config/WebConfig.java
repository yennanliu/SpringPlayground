package com.yen.SpringBootPart1.config;

// https://www.youtube.com/watch?v=QpZEkzjit7o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=27
// https://www.youtube.com/watch?v=2IBSZvwWq5w&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31
// https://www.youtube.com/watch?v=z0sf_f6sfh4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=37
// https://www.youtube.com/watch?v=NEGzyvm1IBc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=42
// https://www.youtube.com/watch?v=fZpsScyj8XI&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=43

import com.yen.SpringBootPart1.bean.Pet2;
import com.yen.SpringBootPart1.converter.YenMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer { // enable MatrixVariable method 2) : implements WebMvcConfigurer

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        //methodFilter.setMethodParam("_m"); // NOTE !! here we'll override default "_method" to "_m" (in filer). please check index.html
        //methodFilter.setMethodParam("_method");
        return methodFilter;
    }

//    /** enable MatrixVariable method 1) */
//    @Bean
//    public WebMvcConfigurer webMvcConfigurer(){
//        return new WebMvcConfigurer(){
//            @Override
//            public void configurePathMatch(PathMatchConfigurer configurer) {
//
//                UrlPathHelper urlPathHelper = new UrlPathHelper();
//
//                /** NOTE !! we enable MatrixVariable here by set urlPathHelper.setRemoveSemicolonContent(false) */
//                urlPathHelper.setRemoveSemicolonContent(false);
//                configurer.setUrlPathHelper(urlPathHelper);
//            }
//        };
//    }

    /** enable MatrixVariable method 1) */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {

            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {

                UrlPathHelper urlPathHelper = new UrlPathHelper();

                /** NOTE !! we enable MatrixVariable here by set urlPathHelper.setRemoveSemicolonContent(false) */
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(new YenMessageConverter());
            }

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

                /** NOTE !!!
                 *  User-defined Content Negotiation
                 *   1) need to put Map<String, MediaType> mediaTypes type in ParameterContentNegotiationStrategy
                 */
                Map<String, MediaType> mediaTypes = new HashMap<>();

                // define which media types are supported
                mediaTypes.put("json", MediaType.APPLICATION_JSON);
                mediaTypes.put("xml", MediaType.APPLICATION_XML);
                mediaTypes.put("yen", MediaType.parseMediaType("application/x-yen"));

                ParameterContentNegotiationStrategy parameterStrategy =  new ParameterContentNegotiationStrategy(mediaTypes);
                configurer.strategies(Arrays.asList(parameterStrategy));
            }
        };
    }

    /** enable MatrixVariable method 2) */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        UrlPathHelper urlPathHelper = new UrlPathHelper();

        /** NOTE !! we enable MatrixVariable here by set setRemoveSemicolonContent = false */
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    // we can create Customized formatter via below Override
    @Override
    public void addFormatters(FormatterRegistry registry) {
        
        registry.addConverter(new Converter<String, Pet2>() {

            @Override
            public Pet2 convert(String source) {
                /**
                 *  index.html :
                 *
                 *  (寵物： <input name="pet" value="lucky,3"/>)
                 *  source : value = "lucky,3"
                 *  -> we need to transform above to name, age
                 */
                if (!StringUtils.isEmpty(source)){
                    Pet2 pet = new Pet2();
                    String[] split = source.split(",");
                    pet.setName(split[0]); // pet's name
                    pet.setAge(Integer.parseInt(split[1])); // pet's age
                    return pet;
                }
                return null;
            }
        });
    }

}
