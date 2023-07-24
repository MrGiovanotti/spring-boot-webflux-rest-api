package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Category;

import reactor.core.publisher.Mono;

public interface CategoryService {
    
    Mono<Category> save(Category category);
    
}
