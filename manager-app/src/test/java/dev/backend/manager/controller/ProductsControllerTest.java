package dev.backend.manager.controller;

import dev.backend.manager.client.BadRequestException;
import dev.backend.manager.client.ProductsRestClient;
import dev.backend.manager.controller.payload.NewProductPayload;
import dev.backend.manager.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;

import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductsController unit tests")
public class ProductsControllerTest {
    @Mock
    ProductsRestClient productsRestClient;

    @InjectMocks
    ProductsController controller;

    @Test
    void getProductsList_ReturnsProductsListPage() {
        // given
        var model = new ConcurrentModel();
        var filter = "product";

        var products = IntStream.range(1, 4)
                .mapToObj(i -> new Product(i, "Product #%d".formatted(i),
                        "Product description №%d".formatted(i)))
                .toList();

        doReturn(products).when(this.productsRestClient).findAllProducts(filter);

        // when
        var result = this.controller.getProductsList(filter, model);

        // then
        assertEquals("catalogue/products/list", result);
        assertEquals(filter, model.getAttribute("filter"));
        assertEquals(products, model.getAttribute("products"));
    }

    @Test
    void getNewProductPage_ReturnsNewProductPage () {
        // given

        // when
        var result = this.controller.getNewProductPage();

        // then
        assertEquals("catalogue/products/create", result);
    }

    @Test
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        // given
        var payload = new NewProductPayload("New product", "New product description");
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doReturn(new Product(1, "New product", "New product description"))
                .when(this.productsRestClient)
                .createProduct("New product", "New product description");

        // when
        var result = this.controller.createProduct(payload, model, response);

        // then
        assertEquals("redirect:/catalogue/products/1", result);

        verify(this.productsRestClient).createProduct("New product", "New product description");
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsProductFormWithErrors() {
        // given
        var payload = new NewProductPayload("  ", null);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(Map.of("Error 1", "Value 1", "Error 2", "Value 2")))
                .when(this.productsRestClient)
                .createProduct("  ", null);

        // when
        var result = this.controller.createProduct(payload, model, response);

        // then
        assertEquals("catalogue/products/create", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(Map.of("Error 1", "Value 1", "Error 2", "Value 2"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.productsRestClient).createProduct("  ", null);
        verifyNoMoreInteractions(this.productsRestClient);
    }
}
