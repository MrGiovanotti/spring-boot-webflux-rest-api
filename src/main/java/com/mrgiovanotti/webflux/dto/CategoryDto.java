package com.mrgiovanotti.webflux.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    
    public CategoryDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
