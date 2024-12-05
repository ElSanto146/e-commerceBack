package com.app.e_commerce.repository;

import com.app.e_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByNameAndPrice (String name, Double price);

    //boolean existsProductByNameAndPrice(String name, Double price);
}
