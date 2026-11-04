package dev.backend.catalogue.catalogue;

import dev.backend.catalogue.catalogue.payload.NewProductPayload;
import dev.backend.catalogue.entity.Product;
import dev.backend.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findProducts(
            @RequestParam(name = "filter", required = false) String filter
     ) {
        return productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody NewProductPayload payload,
//            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale
    ) throws BindException {
//        System.out.println("binding: " + bindingResult);
//        if(bindingResult.hasErrors()) {
//            throw new BindException(bindingResult);
//        }

        Product product = productService.createProduct(payload.title(), payload.details());
        return ResponseEntity
                .created(uriComponentsBuilder
                        .replacePath("/catalogue-api/products/{productId}")
//                        .pathSegment("{productId}") // same
                        .build(Map.of("productId", product.getId())))
                .body(product);
    }
}
