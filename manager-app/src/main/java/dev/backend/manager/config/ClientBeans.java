package dev.backend.manager.config;

import dev.backend.manager.client.RestClientProductsRestClient;
import dev.backend.manager.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
//@PropertySource("classpath:/application-standalone.yml")
public class ClientBeans {

//    @Value("${prodshop.service.catalogue.uri}")
//    private String catalogueBaseUri;

//    @Value("${prodshop.service.catalogue.registration-id}")
//    private String registrationId = "keycloak";

    @Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${prodshop.service.catalogue.uri}") String catalogueBaseUri,
            @Value("${prodshop.service.catalogue.registration-id}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(
                        new OAuthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                        authorizedClientRepository), registrationId))
                .build());
    }
}
