package com.mrgiovanotti.webflux.services.impl;

import org.springframework.stereotype.Service;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.repositories.CategoryRepository;
import com.mrgiovanotti.webflux.services.CategoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public Mono<Category> save(Category category) {
        return categoryRepository.save(category);
    }
    
}
