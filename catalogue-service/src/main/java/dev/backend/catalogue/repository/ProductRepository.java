package dev.backend.catalogue.repository;

import dev.backend.catalogue.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long> {
//    Iterable<Product> findByTitleContainingIgnoreCase(String filter);

//    @Query("select p from  Product p where p.title ilike concat('%', :title, '%')")
//    @Query("select p from  Product p where p.title ilike %:title%")
//    Iterable<Product> findByTitleContainingIgnoreCase(@Param("title") String filter);

    @Query(name = "Product.findByTitleContainingIgnoreCase")
    Iterable<Product> findByTitleContainingIgnoreCase(@Param("title") String filter);

//    List<Product> findAll();
//
//    Product save(Product product);
//
//    Optional<Product> findById(Integer id);
//
//    void deleteById(Integer id);
}
