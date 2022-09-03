package com.wudimanong.authserver.config;

import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
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

/**
 * @author jiangqiao
 */
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
