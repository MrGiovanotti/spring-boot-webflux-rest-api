package com.mrgiovanotti.webflux.services.impl;

import org.springframework.stereotype.Service;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.repositories.CategoryRepository;
import com.mrgiovanotti.webflux.repositories.ProductRepository;
import com.mrgiovanotti.webflux.services.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        
        // Going to DB to retrieve the category
        Mono<Category> categoryMono = categoryRepository.findById(productDto.getCategoryDto().getId());
        
        // Saving Product
        return categoryMono.flatMap(category -> {
            Product product = new Product(productDto.getName(), productDto.getPrice(), category);
            return productRepository.save(product)
                    .map(ProductDto::new);
        }).switchIfEmpty(Mono.empty());
        
    }
    
    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll().map(ProductDto::new);
    }
    
    @Override
    public Mono<ProductDto> findById(String id) {
        return productRepository.findById(id).map(ProductDto::new);
    }
    
    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }
    
}
