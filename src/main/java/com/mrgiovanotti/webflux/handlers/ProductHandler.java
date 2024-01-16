package com.mrgiovanotti.webflux.handlers;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mrgiovanotti.webflux.ApplicationProperties;
import com.mrgiovanotti.webflux.dto.CategoryDto;
import com.mrgiovanotti.webflux.dto.ProductDto;
import com.mrgiovanotti.webflux.services.CategoryService;
import com.mrgiovanotti.webflux.services.ProductService;
import com.mrgiovanotti.webflux.utils.Utils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Esto vendría a ser como nuestro controlador
 *
 * @author mrgiovanotti
 *
 */
@Component
@RequiredArgsConstructor
public class ProductHandler {
    
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ApplicationProperties properties;
    private final Validator validator;
    
    public Mono<ServerResponse> list() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), ProductDto.class);
    }
    
    @SuppressWarnings("deprecation")
    public Mono<ServerResponse> view(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id)
                .flatMap(productDto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(productDto)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    @SuppressWarnings("deprecation")
    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<ProductDto> monoProductDto = request.bodyToMono(ProductDto.class);
        return monoProductDto.flatMap(productDto -> {
            Errors errors = new BeanPropertyBindingResult(productDto, ProductDto.class.getName());
            validator.validate(productDto, errors);
            
            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> ServerResponse.badRequest().body(BodyInserters.fromObject(list)));
            }
            
            return productService.save(productDto)
                    .flatMap(prodDto -> ServerResponse
                            .created(URI.create("/api/v2/product/" + prodDto.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(prodDto)))
                    .switchIfEmpty(ServerResponse.notFound().build());
            
        });
        
    }
    
    @SuppressWarnings("deprecation")
    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ProductDto> monoProductDto = request.bodyToMono(ProductDto.class);
        return monoProductDto.flatMap(productService::update)
                .flatMap(productDto -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(productDto)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }
    
    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findProductById(id)
                .flatMap(product -> productService.delete(product).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    @SuppressWarnings("deprecation")
    public Mono<ServerResponse> upload(ServerRequest request) {
        
        String id = request.pathVariable("id");
        
        return request.multipartData().map(multiValueMap -> multiValueMap.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> productService.findById(id).flatMap(productDto -> {
                    productDto.setPhoto(UUID.randomUUID() + Utils.cleanFileName(file.filename()));
                    return file.transferTo(new File(properties.getImagesUploadPath() + productDto.getPhoto()))
                            .then(productService.save(productDto));
                })).flatMap(prodDto -> ServerResponse.ok().body(BodyInserters.fromObject(prodDto)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    @SuppressWarnings("deprecation")
    public Mono<ServerResponse> createAndUpload(ServerRequest request) {
        
        Mono<ProductDto> monoProductDto = request.multipartData().flatMap(multipart -> {
            FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
            FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
            FormFieldPart categoryId = (FormFieldPart) multipart.toSingleValueMap().get("categoryDto.id");
            
            return categoryService.findById(categoryId.value())
                    .map(category -> {
                        CategoryDto categoryDto = new CategoryDto();
                        categoryDto.setId(category.getId());
                        ProductDto productDto = new ProductDto();
                        productDto.setName(name.value());
                        productDto.setPrice(Double.parseDouble(price.value()));
                        productDto.setCategoryDto(categoryDto);
                        return productDto;
                    });
        });
        
        return request.multipartData().map(multiValueMap -> multiValueMap.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> monoProductDto.flatMap(productDto -> {
                    productDto.setPhoto(UUID.randomUUID() + Utils.cleanFileName(file.filename()));
                    return file.transferTo(new File(properties.getImagesUploadPath() + productDto.getPhoto()))
                            .then(productService.save(productDto));
                })).flatMap(prodDto -> ServerResponse.ok().body(BodyInserters.fromObject(prodDto)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
