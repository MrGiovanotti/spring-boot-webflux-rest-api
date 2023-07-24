package com.mrgiovanotti.webflux.services.impl;

import org.springframework.stereotype.Service;

import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.repositories.ProductRepository;
import com.mrgiovanotti.webflux.services.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }
    
}
