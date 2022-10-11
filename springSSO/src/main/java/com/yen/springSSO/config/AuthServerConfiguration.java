package com.yen.springSSO.config;

// book p. 3-18

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    AuthenticationManager authenticationManager;

    // constructor
    public AuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        this.authenticationManager =
                authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    /** client info op via JDBC */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //super.configure(clients);
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /** endpoint auth setting */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        //super.configure(endpoints);

        // set up endpoint param
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getJdbcTokenStore());

        // support token refresh
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);

        // set up accessToken valid time, use 30 days here
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));

        // set up refreshToken valid time, use 30 days here
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15));

        tokenServices.setClientDetailsService(getClientDetailsService());

        // DB auth info
        endpoints.authenticationManager( this.authenticationManager )
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(getJdbcTokenStore())
                .tokenServices(tokenServices)
                .authorizationCodeServices(getJdbcAuthorizationCodeServices())
                .approvalStore(getJdbcApprovalStore());
    }

    /** DB token bean */
    @Bean
    public JdbcTokenStore getJdbcTokenStore(){

        return new JdbcTokenStore(dataSource);
    }

    /** DB token client info */
    @Bean
    public ClientDetailsService getClientDetailsService(){

        return new JdbcClientDetailsService(dataSource);
    }

    /** DB auth code */
    @Bean
    public AuthorizationCodeServices getJdbcAuthorizationCodeServices(){

        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /** DB auth code confirmation record */
    @Bean
    public ApprovalStore getJdbcApprovalStore(){

        return new JdbcApprovalStore(dataSource);
    }

    /** Access token grant management (encrypt via asymmetric algorithm) */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){

        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // import certification
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("keystore.jks"),
                "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }

}
