package dev.backend.manager.config;

import dev.backend.manager.client.RestClientProductsRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource("classpath:/application-standalone.yml")
public class ClientBeans {

    @Value("${prodshop.service.catalogue.uri}")
    private String baseUrl;

    @Value("${prodshop.service.catalogue.username:username}")
    private String catalogueUsername;

    @Value("${prodshop.service.catalogue.password:password}")
    private String cataloguePassword;

    @Bean
    public RestClientProductsRestClient productsRestClient() {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(new BasicAuthenticationInterceptor(catalogueUsername, cataloguePassword))
                .build());
    }
}
