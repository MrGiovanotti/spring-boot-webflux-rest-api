package com.mrgiovanotti.webflux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mrgiovanotti.webflux.dto.CategoryDto;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.services.CategoryService;
import com.mrgiovanotti.webflux.services.ProductService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxRestApiApplicationTests {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Test
    void testList() {
        
        webTestClient.get()
                .uri("/api/v2/product/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // Se envía la petición
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductDto.class)
                // .hasSize(9) (Abajo otra forma)
                .consumeWith(response -> {
                    List<ProductDto> productsDto = response.getResponseBody();
                    assertThat(productsDto).isNotEmpty();
                });
        
    }
    
    @Test
    void testView() {
        // block() convierte en síncrono, ya que las pruebas no pueden ser asíncronas
        ProductDto productDto = productService.findByName("TV Panasonic Pantalla LCD").block();
        
        webTestClient.get()
                .uri("/api/v2/product/{id}", Collections.singletonMap("id", productDto.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }
    
    @Test
    void testCreate() {
        
        CategoryDto categoryDto = categoryService.findByName("Furniture").block();
        ProductDto productDto = new ProductDto(null, "Mesa decorativa", 105.78, null, categoryDto);
        
        webTestClient.post()
                .uri("/api/v2/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(productDto), ProductDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductDto.class)
                .consumeWith(response -> {
                    ProductDto receivedProductDto = response.getResponseBody();
                    assertThat(receivedProductDto.getId()).isNotEmpty();
                    assertEquals("Mesa decorativa", receivedProductDto.getName());
                    assertEquals(105.78, receivedProductDto.getPrice());
                    assertNotNull(receivedProductDto.getCategoryDto());
                });
    }
    
    @Test
    void testEdit() {
        
        CategoryDto categoryDto = categoryService.findByName("Furniture").block();
        ProductDto productDto = productService.findByName("Hewlett Packard Multifuncional").block();
        ProductDto editedProductDto = new ProductDto(productDto.getId(), "Mesa plegable", 500.00, null, categoryDto);
        
        webTestClient.put().uri("/api/v2/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(editedProductDto), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Mesa plegable")
                .jsonPath("$.categoryDto.name").isEqualTo("Furniture");
        
    }
    
    @Test
    void testDelete() {
        
        ProductDto productToDelete = productService.findByName("Apple Ipod").block();
        
        webTestClient.delete()
                .uri("/api/v2/product/{id}", Collections.singletonMap("id", productToDelete.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
        
        webTestClient.get()
                .uri("/api/v2/product/{id}", Collections.singletonMap("id", productToDelete.getId()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .isEmpty();
        
    }
    
}
