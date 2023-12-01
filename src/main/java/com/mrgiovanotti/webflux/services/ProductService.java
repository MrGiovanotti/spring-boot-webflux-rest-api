package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.dto.ProductDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    
    Flux<ProductDto> findAll();
    
    Mono<ProductDto> findById(String id);
    
    Mono<Product> findProductById(String id);
    
    Mono<ProductDto> save(ProductDto productDto);
    
    // Just for using in run method
    Mono<Product> save(Product product);
    
    Mono<ProductDto> update(ProductDto productDto);
    
    Mono<Void> delete(Product product);
    
}
