package com.mrgiovanotti.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mrgiovanotti.webflux.documents.Product;

import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    
    Mono<Product> findByName(String name);
    
}
