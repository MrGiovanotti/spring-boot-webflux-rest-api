package com.mrgiovanotti.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.mrgiovanotti.webflux.dto.CategoryDto;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.services.CategoryService;
import com.mrgiovanotti.webflux.services.ProductService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxRestApiApplication implements CommandLineRunner {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    CategoryService categoryService;
    
    // Lo vamos a utilizar para borrar la collection cada vez que ejecutamos
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxRestApiApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        // Deleting collections, so we'll avoid duplicates
        reactiveMongoTemplate.dropCollection("products").subscribe();
        reactiveMongoTemplate.dropCollection("categories").subscribe();
        
        CategoryDto electronicCategory = new CategoryDto("Electronic");
        CategoryDto sportCategory = new CategoryDto("Sport");
        CategoryDto computationCategory = new CategoryDto("Computation");
        CategoryDto furnitureCategory = new CategoryDto("Furniture");
        
        Flux<CategoryDto> categoriesFlux = Flux.just(
                electronicCategory,
                sportCategory,
                computationCategory,
                furnitureCategory);
        
        Flux<ProductDto> productsFlux = Flux.just(
                new ProductDto("TV Panasonic Pantalla LCD", 466.89, electronicCategory),
                new ProductDto("Sony Cámara HD Digital", 177.89, electronicCategory),
                new ProductDto("Apple Ipod", 46.89, electronicCategory),
                new ProductDto("Sonny Notebook", 846.89, computationCategory),
                new ProductDto("Hewlett Packard Multifuncional", 200.89, computationCategory),
                new ProductDto("Bianchi Bicicleta", 70.89, sportCategory),
                new ProductDto("HP Notebook Omen 17", 2500.89, computationCategory),
                new ProductDto("Mica Cómoda 5 Cajones", 150.89, furnitureCategory),
                new ProductDto("TV Sonny Bravia OLED 4K Ultra HD", 2255.89, electronicCategory));
        
        categoriesFlux.flatMap(categoryService::save)
                .thenMany(productsFlux.flatMap(productService::save))
                .subscribe();
        
    }
    
}
