package com.mrgiovanotti.webflux.controllers;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.services.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping("/")
    public Mono<ResponseEntity<Flux<ProductDto>>> list() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findAll()));
    }
    
    @GetMapping("/view/{id}")
    public Mono<ResponseEntity<ProductDto>> view(@PathVariable String id) {
        
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
        
    }
    
    @PostMapping("/create")
    public Mono<ResponseEntity<ProductDto>> save(@RequestBody ProductDto productDto) {
        return productService.save(productDto)
                .map(p -> ResponseEntity.created(URI.create("/api/v1/product/view/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }
    
}
