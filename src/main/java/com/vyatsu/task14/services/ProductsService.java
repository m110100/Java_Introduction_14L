package com.vyatsu.task14.services;

import com.vyatsu.task14.entities.Filter;
import com.vyatsu.task14.entities.Product;
import com.vyatsu.task14.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getFilteredProducts(Pattern pattern, Filter filter) {
        List<Product> products = new ArrayList<>();

        for (Product p: getAllProducts()) {
            if (pattern.matcher(p.getTitle()).find()){
                products.add(p);
            }
        }

        products = products.stream().filter(p -> p.getPrice() > filter.getMin() && p.getPrice() < filter.getMax()).collect(Collectors.toList());

        return products;
    }

    public void add(Product product) {
        productRepository.save(product);
    }
    public int remove(Product product){
        if(productRepository.remove(product) == 0){
            return 0;
        }
        else {
            return 1;
        }
    }
}
