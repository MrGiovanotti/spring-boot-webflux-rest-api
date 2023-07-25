package com.mrgiovanotti.webflux.dto;

import java.io.Serializable;

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
    
    public ProductDto(String name, Double price, CategoryDto categoryDto) {
        this.name = name;
        this.price = price;
        this.categoryDto = categoryDto;
    }
    
}
