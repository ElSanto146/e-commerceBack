package com.app.e_commerce.service;

import com.app.e_commerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> allProduct();

    Optional<Product> findProduct(Long id);

    Product createProduct(Product product);

    void deleteProduct(Long id);

    Optional<Product> findProductByNameAndPrice(String name, Double price);

    boolean existsProductByNameAndPrice(String name, Double price);
}
