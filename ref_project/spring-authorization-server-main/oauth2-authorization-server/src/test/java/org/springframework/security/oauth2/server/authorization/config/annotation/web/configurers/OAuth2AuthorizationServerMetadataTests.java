/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers;

import java.util.function.Consumer;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.TestJwks;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadata;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.TestRegisteredClients;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.test.SpringTestContext;
import org.springframework.security.oauth2.server.authorization.test.SpringTestContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the OAuth 2.0 Authorization Server Metadata endpoint.
 *
 * @author Daniel Garnier-Moiroux
 */
@ExtendWith(SpringTestContextExtension.class)
public class OAuth2AuthorizationServerMetadataTests {
	private static final String DEFAULT_OAUTH2_AUTHORIZATION_SERVER_METADATA_ENDPOINT_URI = "/.well-known/oauth-authorization-server";
	private static final String issuerUrl = "https://example.com/issuer1";
	private static EmbeddedDatabase db;
	private static JWKSource<SecurityContext> jwkSource;

	public final SpringTestContext spring = new SpringTestContext();

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JdbcOperations jdbcOperations;

	@BeforeAll
	public static void setupClass() {
		JWKSet jwkSet = new JWKSet(TestJwks.DEFAULT_RSA_JWK);
		jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
		db = new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.HSQL)
				.setScriptEncoding("UTF-8")
				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
				.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
				.build();
	}

	@AfterEach
	public void tearDown() {
		jdbcOperations.update("truncate table oauth2_authorization");
		jdbcOperations.update("truncate table oauth2_registered_client");
	}

	@AfterAll
	public static void destroy() {
		db.shutdown();
	}

	@Test
	public void requestWhenAuthorizationServerMetadataRequestAndIssuerSetThenUsed() throws Exception {
		this.spring.register(AuthorizationServerConfiguration.class).autowire();

		this.mvc.perform(get(DEFAULT_OAUTH2_AUTHORIZATION_SERVER_METADATA_ENDPOINT_URI))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("issuer").value(issuerUrl))
				.andReturn();
	}

	@Test
	public void requestWhenAuthorizationServerMetadataRequestAndIssuerNotSetThenResolveFromRequest() throws Exception {
		this.spring.register(AuthorizationServerConfigurationWithIssuerNotSet.class).autowire();

		this.mvc.perform(get(DEFAULT_OAUTH2_AUTHORIZATION_SERVER_METADATA_ENDPOINT_URI))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("issuer").value("http://localhost"))
				.andReturn();
	}

	// gh-616
	@Test
	public void requestWhenAuthorizationServerMetadataRequestAndMetadataCustomizerSetThenReturnCustomMetadataResponse() throws Exception {
		this.spring.register(AuthorizationServerConfigurationWithMetadataCustomizer.class).autowire();

		this.mvc.perform(get(DEFAULT_OAUTH2_AUTHORIZATION_SERVER_METADATA_ENDPOINT_URI))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath(OAuth2AuthorizationServerMetadataClaimNames.SCOPES_SUPPORTED,
						hasItems("scope1", "scope2")));
	}

	@EnableWebSecurity
	@Import(OAuth2AuthorizationServerConfiguration.class)
	static class AuthorizationServerConfiguration {

		@Bean
		RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
			RegisteredClient registeredClient = TestRegisteredClients.registeredClient().build();
			JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcOperations);
			registeredClientRepository.save(registeredClient);
			return registeredClientRepository;
		}

		@Bean
		JdbcOperations jdbcOperations() {
			return new JdbcTemplate(db);
		}

		@Bean
		JWKSource<SecurityContext> jwkSource() {
			return jwkSource;
		}

		@Bean
		AuthorizationServerSettings authorizationServerSettings() {
			return AuthorizationServerSettings.builder().issuer(issuerUrl).build();
		}
	}

	@EnableWebSecurity
	@Configuration(proxyBeanMethods = false)
	static class AuthorizationServerConfigurationWithMetadataCustomizer extends AuthorizationServerConfiguration {

		// @formatter:off
		@Bean
		public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
			OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
					new OAuth2AuthorizationServerConfigurer();
			http.apply(authorizationServerConfigurer);

			authorizationServerConfigurer
					.authorizationServerMetadataEndpoint(authorizationServerMetadataEndpoint ->
							authorizationServerMetadataEndpoint
									.authorizationServerMetadataCustomizer(authorizationServerMetadataCustomizer()));

			RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

			http
					.securityMatcher(endpointsMatcher)
					.authorizeHttpRequests(authorize ->
							authorize.anyRequest().authenticated()
					)
					.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));

			return http.build();
		}
		// @formatter:on

		private Consumer<OAuth2AuthorizationServerMetadata.Builder> authorizationServerMetadataCustomizer() {
			return (authorizationServerMetadata) ->
					authorizationServerMetadata.scope("scope1").scope("scope2");
		}

	}

	@EnableWebSecurity
	@Import(OAuth2AuthorizationServerConfiguration.class)
	static class AuthorizationServerConfigurationWithIssuerNotSet extends AuthorizationServerConfiguration {

		@Bean
		AuthorizationServerSettings authorizationServerSettings() {
			return AuthorizationServerSettings.builder().build();
		}
	}

}
