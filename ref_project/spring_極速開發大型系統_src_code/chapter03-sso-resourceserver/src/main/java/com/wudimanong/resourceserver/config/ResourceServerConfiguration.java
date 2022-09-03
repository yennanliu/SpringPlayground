package com.wudimanong.resourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author jiangqiao
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 验证Token令牌授权验证服务器地址
     */
    @Value("${security.oauth2.checkTokenUrl}")
    private String checkTokenUrl;
    /**
     * 受信任访问的客户端ID
     */
    @Value("${security.oauth2.clientId}")
    private String clientId;
    /**
     * 受信任访问的客户端密钥
     */
    @Value("${security.oauth2.clientSecret}")
    private String clientSecret;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(checkTokenUrl);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        resources.tokenServices(tokenService);
    }
}
