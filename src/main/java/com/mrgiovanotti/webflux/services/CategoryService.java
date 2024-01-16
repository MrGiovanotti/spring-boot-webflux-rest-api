package com.mrgiovanotti.webflux.services;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.dto.CategoryDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    
    Flux<CategoryDto> findAll();
    
    Mono<Category> findById(String id);
    
    Mono<Category> save(CategoryDto categoryDto);
    
    Mono<Category> save(Category category);
    
    Mono<CategoryDto> findByName(String name);
    
}
