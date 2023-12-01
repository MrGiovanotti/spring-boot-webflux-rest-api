package com.mrgiovanotti.webflux.mappers;

import com.mrgiovanotti.webflux.documents.Product;
import com.mrgiovanotti.webflux.dto.ProductDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {
    
    public static ProductDto map(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setPhoto(product.getPhoto());
        productDto.setCategoryDto(CategoryMapper.map(product.getCategory()));
        return productDto;
    }
    
    public static Product map(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setPhoto(productDto.getPhoto());
        product.setCategory(CategoryMapper.map(productDto.getCategoryDto()));
        return product;
    }
    
}
