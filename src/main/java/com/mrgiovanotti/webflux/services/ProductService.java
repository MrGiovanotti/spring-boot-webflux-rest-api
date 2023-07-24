package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Product;

import reactor.core.publisher.Mono;

public interface ProductService {
    
    Mono<Product> save(Product product);
    
}
