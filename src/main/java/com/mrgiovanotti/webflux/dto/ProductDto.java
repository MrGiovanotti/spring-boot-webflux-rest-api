package com.mrgiovanotti.webflux.dto;

import java.io.Serializable;

import com.mrgiovanotti.webflux.documents.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String name;
    private Double price;
    
    private CategoryDto categoryDto;
    
    public ProductDto(String id, String name, Double price, CategoryDto categoryDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryDto = categoryDto;
    }
    
    public ProductDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), new CategoryDto(product.getCategory()));
    }
    
}
