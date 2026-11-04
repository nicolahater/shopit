package dev.backend.manager.client;

import dev.backend.manager.entity.Product;

import java.util.List;
import java.util.Optional;

// TODO: or instead of this interface we could just do implementation of ProductService interface.
//  Like public class ProductRestClient implements ProductService
public interface ProductsRestClient {
    List<Product> findAllProducts(String filter);
    Product createProduct(String title, String details);
    Optional<Product> findProduct(int productId);
    void updateProduct(int productId, String title, String details);
    void deleteProduct(int productId);
}
