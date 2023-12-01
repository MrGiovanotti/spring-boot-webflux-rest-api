package com.mrgiovanotti.webflux.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    @NotNull
    private String name;
    
    @NotNull
    private Double price;
    
    private String photo;
    
    @NotNull
    private CategoryDto categoryDto;
    
    public ProductDto(String id, String name, Double price, String photo, CategoryDto categoryDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.photo = photo;
        this.categoryDto = categoryDto;
    }
    
}
