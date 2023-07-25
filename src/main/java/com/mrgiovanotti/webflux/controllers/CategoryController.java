package com.mrgiovanotti.webflux.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.services.CategoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @GetMapping("/")
    public Mono<ResponseEntity<Flux<Category>>> findAll() {
        
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.findAll()));
        
    }
    
}
