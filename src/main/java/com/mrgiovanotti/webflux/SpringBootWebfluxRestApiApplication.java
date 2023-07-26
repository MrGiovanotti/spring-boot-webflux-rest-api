package com.mrgiovanotti.webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.mrgiovanotti.webflux.documents.Category;
import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.services.CategoryService;
import com.mrgiovanotti.webflux.services.ProductService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxRestApiApplication implements CommandLineRunner {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootWebfluxRestApiApplication.class);
    
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
        
        Category electronicCategory = new Category("Electronic");
        Category sportCategory = new Category("Sport");
        Category computationCategory = new Category("Computation");
        Category furnitureCategory = new Category("Furniture");
        
        Flux<Category> categoriesFlux = Flux.just(
                electronicCategory,
                sportCategory,
                computationCategory,
                furnitureCategory);
        
        Flux<Product> productsFlux = Flux.just(
                new Product("TV Panasonic Pantalla LCD", 466.89, electronicCategory),
                new Product("Sony Cámara HD Digital", 177.89, electronicCategory),
                new Product("Apple Ipod", 46.89, electronicCategory),
                new Product("Sonny Notebook", 846.89, computationCategory),
                new Product("Hewlett Packard Multifuncional", 200.89, computationCategory),
                new Product("Bianchi Bicicleta", 70.89, sportCategory),
                new Product("HP Notebook Omen 17", 2500.89, computationCategory),
                new Product("Mica Cómoda 5 Cajones", 150.89, furnitureCategory),
                new Product("TV Sonny Bravia OLED 4K Ultra HD", 2255.89, electronicCategory));
        
        categoriesFlux.flatMap(categoryService::save)
                .doOnNext(cat -> LOGGER.info("Guardando categoría: {}", cat.getId()))
                .thenMany(productsFlux.flatMap(productService::save))
                .subscribe(prod -> LOGGER.info("Guardando producto: {}", prod.getId()));
        
    }
    
}
