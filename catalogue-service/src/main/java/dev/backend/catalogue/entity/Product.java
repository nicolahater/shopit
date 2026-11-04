package dev.backend.catalogue.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "catalogue", name="t_product")
@NamedQueries(
    @NamedQuery(
            name = "Product.findByTitleContainingIgnoreCase",
            query = "select p from Product p where p.title ilike concat('%', :title, '%')"
    )
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "c_title")
    private String title;

    @Size(max = 1000)
    @Column(name = "c_details")
    private String details;
}
