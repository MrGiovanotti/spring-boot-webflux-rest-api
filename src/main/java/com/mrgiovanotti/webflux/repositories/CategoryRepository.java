package com.mrgiovanotti.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mrgiovanotti.webflux.documents.Category;

import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    
    Mono<Category> findByName(String name);
    
}
