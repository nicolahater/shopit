package dev.backend.catalogue.catalogue;

import dev.backend.catalogue.catalogue.payload.UpdateProductPayload;
import dev.backend.catalogue.entity.Product;
import dev.backend.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products/{productId:\\d+}")
public class ProductRestController {
    private final ProductService productService;

    @ModelAttribute
    public Product getProduct(@PathVariable("productId") int productId) {
        Product product = productService.findProduct(productId).orElseThrow(
                () -> new NoSuchElementException("catalogue.errors.product.not_found"));
        return product;
    }

    // TODO: check
//    @GetMapping("/{some}")
//    public String getSomeInt(@PathVariable("some") Integer some) {
//        return "hello, Integer";
//    }

//    // TODO: check
//    @GetMapping("/{some}")
//    public String getSomeStr(@PathVariable("some") String some) {
//        return "hello, String";
//    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(
            @PathVariable("productId") int productId,
            @Valid @RequestBody UpdateProductPayload payload
    ) {
        productService.updateProduct(productId, payload.title(), payload.details());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("productId") int productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
