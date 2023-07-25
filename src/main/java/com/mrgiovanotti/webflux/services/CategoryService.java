package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.dto.CategoryDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    
    Mono<Category> findById(String id);
    
    Mono<Category> save(CategoryDto categoryDto);
    
    Flux<Category> findAll();
    
}
