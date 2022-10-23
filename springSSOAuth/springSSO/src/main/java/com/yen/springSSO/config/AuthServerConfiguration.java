package com.yen.springSSO.config;

// book p. 3-18
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/java/com/wudimanong/authserver/config/AuthServerConfiguration.java

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

    /**
     * JDBC数据源依赖
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 授权认证管理接口
     */
    AuthenticationManager authenticationManager;

    /**
     * 构造方法
     *
     * @param authenticationConfiguration
     * @throws Exception
     */
    public AuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 使用JDBC数据库方式实现客户端信息管理
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 授权认证服务器相关服务端点配置
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //配置TokenService参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getJdbcTokenStore());
        //支持token刷新
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        //accessToken有效时间，这里设置为30天
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        //refreshToken有效时间,这里设置为15天
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15));
        tokenServices.setClientDetailsService(getJdbcClientDetailsService());
        // 数据库管理授权信息
        endpoints.authenticationManager(this.authenticationManager).accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(getJdbcTokenStore()).tokenServices(tokenServices)
                .authorizationCodeServices(getJdbcAuthorizationCodeServices()).approvalStore(getJdbcApprovalStore());
    }

    /**
     * 安全约束配置
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    /**
     * 数据库管理Token实例
     *
     * @return
     */
    @Bean
    public JdbcTokenStore getJdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 数据库管理客户端信息
     *
     * @return
     */
    @Bean
    public ClientDetailsService getJdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 数据库管理授权码信息
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices getJdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 数据库管理用户授权确认记录
     *
     * @return
     */
    @Bean
    public ApprovalStore getJdbcApprovalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * AccessToken颁发管理(使用非对称加密算法来对Token进行签名)
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 导入证书
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }
}

//
//@Configuration
//@EnableAuthorizationServer
//public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired // TODO : check why we DON'T need @AutoWired here
//    AuthenticationManager authenticationManager;
//
//    // constructor
//    public AuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//
//        this.authenticationManager =
//                authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//
//        security.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
//                .allowFormAuthenticationForClients();
//    }
//
//    /** client info op via JDBC */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        //super.configure(clients);
//        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
//    }
//
//    /** endpoint auth setting */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//
//        //super.configure(endpoints);
//
//        // set up endpoint param
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(getJdbcTokenStore());
//
//        // support token refresh
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setReuseRefreshToken(false);
//
//        // set up accessToken valid time, use 30 days here
//        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
//
//        // set up refreshToken valid time, use 30 days here
//        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15));
//
//        tokenServices.setClientDetailsService(getClientDetailsService());
//
//        // DB auth info
//        endpoints.authenticationManager( this.authenticationManager )
//                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenStore(getJdbcTokenStore())
//                .tokenServices(tokenServices)
//                .authorizationCodeServices(getJdbcAuthorizationCodeServices())
//                .approvalStore(getJdbcApprovalStore());
//    }
//
//    /** DB token bean */
//    @Bean
//    public JdbcTokenStore getJdbcTokenStore(){
//
//        return new JdbcTokenStore(dataSource);
//    }
//
//    /** DB token client info */
//    @Bean
//    public ClientDetailsService getClientDetailsService(){
//
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//    /** DB auth code */
//    @Bean
//    public AuthorizationCodeServices getJdbcAuthorizationCodeServices(){
//
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }
//
//    /** DB auth code confirmation record */
//    @Bean
//    public ApprovalStore getJdbcApprovalStore(){
//
//        return new JdbcApprovalStore(dataSource);
//    }
//
//    /** Access token grant management (encrypt via asymmetric algorithm) */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter(){
//
//        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        // import certification
//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
//                new ClassPathResource("keystore.jks"),
//                "mypass".toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
//        return converter;
//    }
//
//}
