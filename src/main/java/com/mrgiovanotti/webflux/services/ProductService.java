package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.dto.ProductDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    
    Mono<Product> save(ProductDto productDto);
    
    Mono<Product> findById(String id);
    
    Flux<Product> findAll();
    
}
