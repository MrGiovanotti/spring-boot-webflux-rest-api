package com.mrgiovanotti.webflux.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    
    private String name;
    private Double price;
    private Date createdAt;
    private String photo;
    
    private Category category;
    
    public Product(String name, Double price, String photo) {
        this.name = name;
        this.price = price;
        this.photo = photo;
        createdAt = new Date();
    }
    
    public Product(String name, Double price, String photo, Category category) {
        this(name, price, photo);
        this.category = category;
    }
    
}
