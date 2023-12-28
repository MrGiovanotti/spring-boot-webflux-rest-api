package com.mrgiovanotti.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mrgiovanotti.webflux.handlers.ProductHandler;

import lombok.RequiredArgsConstructor;

/**
 * En esta clase de configuración es donde configuraremos los endpoints de
 * nuestra api
 *
 * @author mrgiovanotti
 *
 */

@Configuration
@RequiredArgsConstructor
public class RouterFunctionConfig {
    
    private static final String V2_PRODUCT_ROUTE = "/api/v2/product/";
    private static final String V2_PRODUCT_ID_ROUTE = "/api/v2/product/{id}";
    
    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return RouterFunctions.route(RequestPredicates
                .GET(V2_PRODUCT_ROUTE),
                request -> productHandler.list())
                .andRoute(RequestPredicates.GET(V2_PRODUCT_ID_ROUTE), productHandler::view)
                .andRoute(RequestPredicates.POST(V2_PRODUCT_ROUTE), productHandler::create)
                .andRoute(RequestPredicates.PUT(V2_PRODUCT_ROUTE), productHandler::update)
                .andRoute(RequestPredicates.DELETE(V2_PRODUCT_ID_ROUTE), productHandler::delete)
                .andRoute(RequestPredicates.POST("/api/v2/product/upload/{id}"), productHandler::upload)
                .andRoute(RequestPredicates.POST("/api/v2/product/create-and-upload"), productHandler::createAndUpload);
    }
    
}
