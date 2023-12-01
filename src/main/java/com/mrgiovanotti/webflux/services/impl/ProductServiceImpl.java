package com.mrgiovanotti.webflux.services.impl;

import org.springframework.stereotype.Service;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.mappers.ProductMapper;
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
            Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getPhoto(), category);
            return productRepository.save(product)
                    .map(ProductMapper::map);
        });
        
    }
    
    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll().map(ProductMapper::map);
    }
    
    @Override
    public Mono<ProductDto> findById(String id) {
        return productRepository.findById(id).map(ProductMapper::map);
    }
    
    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public Mono<Product> findProductById(String id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Mono<ProductDto> update(ProductDto productDto) {
        
        Mono<Category> categoryMono = categoryRepository.findById(productDto.getCategoryDto().getId());
        
        return categoryMono.flatMap(category -> productRepository.findById(productDto.getId()).flatMap(product -> {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setCategory(category);
            return productRepository.save(product);
        }).map(ProductMapper::map));
    }
    
    @Override
    public Mono<Void> delete(Product product) {
        return productRepository.delete(product);
    }
    
}
