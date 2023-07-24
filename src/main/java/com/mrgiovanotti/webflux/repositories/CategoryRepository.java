package com.mrgiovanotti.webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mrgiovanotti.webflux.documents.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    
}
