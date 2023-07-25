package com.mrgiovanotti.webflux.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    
    public CategoryDto(String name) {
        this.name = name;
    }
    
}
