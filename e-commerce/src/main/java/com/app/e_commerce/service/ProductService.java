package com.app.e_commerce.service;

import com.app.e_commerce.model.Product;
import com.app.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> allProduct() {
        return productRepo.findAll();
    }

    @Override
    public Optional<Product> findProduct(Long id) {
        return productRepo.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Optional<Product> findProductByNameAndPrice(String name, Double price) {
        return productRepo.findProductByNameAndPrice(name, price);
    }

    @Override
    public boolean existsProductByNameAndPrice(String name, Double price) {
        Optional<Product> existingProduct = productRepo.findProductByNameAndPrice(name, price);
        return existingProduct.isPresent();
    }


}
