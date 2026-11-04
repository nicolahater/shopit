package dev.backend.catalogue.service;

import dev.backend.catalogue.entity.Product;
import dev.backend.catalogue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if(filter == null || filter.isBlank()) return productRepository.findAll();
        return productRepository.findByTitleContainingIgnoreCase(filter);
    }

    @Override
    @Transactional
    public Product createProduct(String title, String details) {
        return productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProduct(Integer id) {
        return productRepository.findById(id.longValue());
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String details) {
        productRepository.findById(Long.valueOf(id)).ifPresentOrElse(product -> {
            product.setTitle(title);
            System.out.println("1 updating....");
            product.setDetails(details);
            System.out.println("2 updating....");
        }, () -> {
            throw new NoSuchElementException();
        });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(Long.valueOf(id));
    }
}
