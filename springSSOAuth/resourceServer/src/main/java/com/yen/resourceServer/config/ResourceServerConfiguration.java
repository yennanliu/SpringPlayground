package com.yen.resourceServer.config;

// book p.3-43

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.checkTokenUrl}")
    private String checkTokenUrl;

    @Value("${security.oauth2.clientId}")
    private String clientId;

    @Value("${security.oauth2.clientSecret}")
    private String clientSecret;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        //super.configure(resources);
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        resources.tokenServices(tokenServices);
    }

}
