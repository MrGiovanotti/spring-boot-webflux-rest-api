package com.mrgiovanotti.webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ApplicationProperties {
    
    @Value("${config.uploads.path}")
    private String imagesUploadPath;
    
}
