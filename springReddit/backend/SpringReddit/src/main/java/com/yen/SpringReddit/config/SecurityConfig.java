package com.yen.SpringReddit.config;

// https://youtu.be/kpKUMmAmcj0?t=63
// https://youtu.be/1ojKQxVssPQ?t=187
// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/config/SecurityConfig.java

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    @Value("${jwt.public.key}")
//    RSAPublicKey publicKey;
//
//    @Value("${jwt.private.key}")
//    RSAPrivateKey privateKey;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //.cors().and()
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/auth/**")
                                .permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/subreddit")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/posts/")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/posts/**")
//                        .permitAll()
//                        .requestMatchers("/v2/api-docs",
//                                "/configuration/ui",
//                                "/swagger-resources/**",
//                                "/configuration/security",
//                                "/swagger-ui.html",
//                                "/webjars/**")
//                        .permitAll()
//                        .anyRequest()
//                        .authenticated()
                )
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
//                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
//                )
                .build();
    }

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
//    }
//
//    @Bean
//    JwtEncoder jwtEncoder() {
//        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
//        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
//        return new NimbusJwtEncoder(jwks);
//    }

}


//// TODO : double check if should use WebSecurityConfigurerAdapter or not
//public class SecurityConfig extends SecurityConfigurerAdapter {
//
//    @Autowired
//    UserDetailsService userDetailsService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
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
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                //.requestMatchers("/api/auth/**")
//                //.permitAll()
//                .anyRequest()
//                .authenticated();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder);
//    }
//
//    // encrypt user password (for saving it in DB)
//    // https://youtu.be/kpKUMmAmcj0?t=297
////    @Bean
////    PasswordEncoder passwordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
//
//}
