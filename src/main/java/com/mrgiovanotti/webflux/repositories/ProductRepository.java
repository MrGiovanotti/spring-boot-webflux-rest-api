package com.mrgiovanotti.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mrgiovanotti.webflux.documents.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    
}
