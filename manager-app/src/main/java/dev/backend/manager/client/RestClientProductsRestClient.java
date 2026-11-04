package dev.backend.manager.client;

import dev.backend.manager.controller.payload.NewProductPayload;
import dev.backend.manager.controller.payload.UpdateProductPayload;
import dev.backend.manager.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductsRestClient {
    private static final String GET_PRODUCTS_URL = "/catalogue-api/products";
    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts(String filter) {
        return restClient
                .get()
//                .uri(GET_PRODUCTS_URL + "?=filter={filter}", filter)
                .uri(uriBuilder -> uriBuilder   // Source: https://stackoverflow.com/questions/77592651/how-to-send-request-parameters-with-the-new-spring-boot-3-2-restclient
                        .path(GET_PRODUCTS_URL)
                        .queryParam("filter", filter)
                        .build()
                )
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Product createProduct(String title, String details) {
        try { // TODO: use Result Pattern
            return restClient
                    .post()
                    .uri("/catalogue-api/products")
                    .body(new NewProductPayload(title, details)) //TODO: here we have to use NewProductPayload from catalogue-service
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);

            if(problemDetail != null && problemDetail.getProperties() != null) {
                throw new BadRequestException((Map<String, String>) problemDetail.getProperties().get("errors"));
            }
            throw new BadRequestException();
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(
                    restClient
                    .get()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class)
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String details) {
        try { // TODO: use Result Pattern
            restClient
                .patch()
                .uri("/catalogue-api/products/{productId}", productId)
                .body(new UpdateProductPayload(title, details)) //TODO: here we have to use NewProductPayload from catalogue-service
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);

            if(problemDetail != null && problemDetail.getProperties() != null) {
                throw new BadRequestException((Map<String, String>) problemDetail.getProperties().get("errors"));
            }
            throw new BadRequestException();
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            restClient
                .delete()
                .uri("/catalogue-api/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException(e);
        }
    }
}
