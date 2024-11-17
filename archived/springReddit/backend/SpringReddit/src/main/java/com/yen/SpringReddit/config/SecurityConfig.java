package com.yen.SpringReddit.config;

import com.yen.SpringReddit.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        /**
         *  NOTE : Need to disable CSRF, otherwise request will be blocked, before reaching controller
         *
         *  // https://juejin.cn/s/403%20forbidden%20error%20post%20request%20spring%20boot
         *  // disable CSRF
         */
        httpSecurity = httpSecurity
                .csrf()
                .disable()
                .cors()
                .disable();

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/signup").permitAll() // ONLY API with permitAll can be accessed without auth
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/hello").permitAll()
                .requestMatchers("/public-api").permitAll()
                //.anyRequest().hasRole("contributors"); // only role "contributors" can be authorized to access /public-api endpoint
                .anyRequest().authenticated(); // can enable this, comment above, make all requests being auto authenticated

        // return Http error 403, instead of 401 if JWT token is not provided
        httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        httpSecurity.addFilterBefore(new JwtTokenFilter(), AuthorizationFilter.class);

        // Not let spring boot manage session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }
}


//
//// https://youtu.be/kpKUMmAmcj0?t=63
//// https://youtu.be/1ojKQxVssPQ?t=187
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/config/SecurityConfig.java
//
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//<<<<<<< HEAD
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//=======
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
//import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
//>>>>>>> ed36b22d4 (refactor securityConfig, update main app annotation)
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
////    @Value("${jwt.public.key}")
////    RSAPublicKey publicKey;
////
////    @Value("${jwt.private.key}")
////    RSAPrivateKey privateKey;
//
//<<<<<<< HEAD
////    @Bean(BeanIds.AUTHENTICATION_MANAGER)
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
////        return authenticationConfiguration.getAuthenticationManager();
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//
//        httpSecurity
//                .authorizeHttpRequests()
//                .requestMatchers("/public-api").permitAll()
//                .anyRequest().authenticated();
//
//        return httpSecurity.build();
//    }
//
////    @Bean
////    public AuthenticationManager authenticationManagerBean() throws Exception {
////        return super.authenticationManagerBean();
////    }
//
////    @Bean
////    public UserDetailsService myUserDetailsService(){
////        return new InMemoryUserDetailsManager(
////                User.withUsername("admin")
////                        .password("{noop}123")
////                        .authorities("READ", "ROLE_USER")
////                        .build());
////    }
//
////    public void configure(HttpSecurity httpSecurity) throws Exception {
////
////        httpSecurity
////                .csrf()
////                .disable()
////                .authorizeRequests()
////                //.requestMatchers("/api/auth/**")
////                //.permitAll()
////                .anyRequest()
////                .authenticated();
////    }
//
////    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
////        authenticationManagerBuilder
////                .userDetailsService(userDetailsService)
////                .passwordEncoder(passwordEncoder);
////    }
//
//    // encrypt user password (for saving it in DB)
//    // https://youtu.be/kpKUMmAmcj0?t=297
//=======
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                //.cors().and()
//                .csrf().disable()
//                .authorizeHttpRequests(authorize -> authorize
//                                .requestMatchers("/api/auth/**")
//                                .permitAll()
////                        .requestMatchers(HttpMethod.GET, "/api/subreddit")
////                        .permitAll()
////                        .requestMatchers(HttpMethod.GET, "/api/posts/")
////                        .permitAll()
////                        .requestMatchers(HttpMethod.GET, "/api/posts/**")
////                        .permitAll()
////                        .requestMatchers("/v2/api-docs",
////                                "/configuration/ui",
////                                "/swagger-resources/**",
////                                "/configuration/security",
////                                "/swagger-ui.html",
////                                "/webjars/**")
////                        .permitAll()
////                        .anyRequest()
////                        .authenticated()
//                )
////                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(exceptions -> exceptions
////                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
////                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
////                )
//                .build();
//    }
//
//>>>>>>> ed36b22d4 (refactor securityConfig, update main app annotation)
////    @Bean
////    PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    JwtDecoder jwtDecoder() {
////        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
////    }
////
////    @Bean
////    JwtEncoder jwtEncoder() {
////        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
////        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
////        return new NimbusJwtEncoder(jwks);
////    }
//
//}
//
//
////// TODO : double check if should use WebSecurityConfigurerAdapter or not
////public class SecurityConfig extends SecurityConfigurerAdapter {
////
////    @Autowired
////    UserDetailsService userDetailsService;
////
////    @Autowired
////    PasswordEncoder passwordEncoder;
////
////    @Bean(BeanIds.AUTHENTICATION_MANAGER)
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
////        return authenticationConfiguration.getAuthenticationManager();
////    }
////
//////    @Bean
//////    public AuthenticationManager authenticationManagerBean() throws Exception {
//////        return super.authenticationManagerBean();
//////    }
////
//////    @Bean
//////    public UserDetailsService myUserDetailsService(){
//////        return new InMemoryUserDetailsManager(
//////                User.withUsername("admin")
//////                        .password("{noop}123")
//////                        .authorities("READ", "ROLE_USER")
//////                        .build());
//////    }
////
////    public void configure(HttpSecurity httpSecurity) throws Exception {
////
////        httpSecurity
////                .csrf()
////                .disable()
////                .authorizeRequests()
////                //.requestMatchers("/api/auth/**")
////                //.permitAll()
////                .anyRequest()
////                .authenticated();
////    }
////
////    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
////        authenticationManagerBuilder
////                .userDetailsService(userDetailsService)
////                .passwordEncoder(passwordEncoder);
////    }
////
////    // encrypt user password (for saving it in DB)
////    // https://youtu.be/kpKUMmAmcj0?t=297
//////    @Bean
//////    PasswordEncoder passwordEncoder(){
//////        return new BCryptPasswordEncoder();
//////    }
////
////}
