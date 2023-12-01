package com.mrgiovanotti.webflux.services.impl;

import org.springframework.stereotype.Service;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.dto.CategoryDto;
import com.mrgiovanotti.webflux.mappers.CategoryMapper;
import com.mrgiovanotti.webflux.repositories.CategoryRepository;
import com.mrgiovanotti.webflux.services.CategoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public Flux<CategoryDto> findAll() {
        
        return categoryRepository.findAll()
                .map(CategoryMapper::map);
        
    }
    
    @Override
    public Mono<Category> findById(String id) {
        return categoryRepository.findById(id);
    }
    
    @Override
    public Mono<Category> save(CategoryDto categoryDto) {
        
        Category category = new Category(categoryDto.getName());
        return categoryRepository.save(category);
    }
    
    @Override
    public Mono<Category> save(Category category) {
        return categoryRepository.save(category);
    }
    
}
