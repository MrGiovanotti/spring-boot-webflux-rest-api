package com.mrgiovanotti.webflux.controllers;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.mrgiovanotti.webflux.ApplicationProperties;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.mappers.ProductMapper;
import com.mrgiovanotti.webflux.services.ProductService;
import com.mrgiovanotti.webflux.utils.Utils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final ApplicationProperties properties;
    
    @GetMapping("/")
    public Mono<ResponseEntity<Flux<ProductDto>>> list() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findAll()));
    }
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> view(@PathVariable String id) {
        
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
        
    }
    
    @PostMapping("/")
    public Mono<ResponseEntity<Map<String, Object>>> create(@Valid @RequestBody Mono<ProductDto> monoProductDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        return monoProductDto.flatMap(productService::save)
                .map(p -> {
                    response.put("product", p);
                    return ResponseEntity.created(URI.create("/api/v1/product/view/" + p.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                })
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                .onErrorResume(t -> Mono.just(t).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                        .flatMapMany(Flux::fromIterable)
                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> {
                            response.put("errors", list);
                            return Mono.just(ResponseEntity.badRequest().body(response));
                        }));
        
    }
    
    @PutMapping("/")
    public Mono<ResponseEntity<ProductDto>> update(@RequestBody ProductDto productDto) {
        return productService.update(productDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productService.findProductById(id)
                .flatMap(product -> productService.delete(product)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<ProductDto>> upload(@PathVariable String id, @RequestPart FilePart file) {
        return productService.findProductById(id).flatMap(product -> {
            product.setPhoto(UUID.randomUUID() + Utils.cleanFileName(file.filename()));
            return file.transferTo(new File(properties.getImagesUploadPath() + product.getPhoto()))
                    .then(productService.save(product));
        }).map(p -> ResponseEntity.ok(ProductMapper.map(p)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/create-and-upload")
    public Mono<ResponseEntity<ProductDto>> createAndUpload(ProductDto productDto, @RequestPart FilePart file) {
        productDto.setPhoto(UUID.randomUUID() + Utils.cleanFileName(file.filename()));
        return file.transferTo(new File(properties.getImagesUploadPath() + productDto.getPhoto()))
                .then(productService.save(productDto)).map(prodDto -> ResponseEntity
                        .created(URI.create(URI.create("/api/v1/product/view/") + prodDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(prodDto))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    
}
