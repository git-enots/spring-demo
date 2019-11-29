package com.router;

import com.handler.CityHander;
import com.handler.UserHander;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AppRouter {
    @Bean
    public RouterFunction<ServerResponse> cityRoutes(CityHander cityHander) {
        return RouterFunctions.route(GET("/api/city/detail/{id}").and(accept(APPLICATION_JSON_UTF8)),cityHander::getCityDetail)
                .andRoute(GET("/api/city/list").and(accept(APPLICATION_JSON_UTF8)), cityHander::listCity)
                .andRoute(POST("/api/city/save").and(contentType(APPLICATION_JSON_UTF8)), cityHander::saveCity)
                .andRoute(DELETE("/api/city/delete/{id}").and(accept(APPLICATION_JSON_UTF8)), cityHander::deleteCity);

    }
    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHander userHander) {
        return RouterFunctions.route(POST("/login").and(contentType(APPLICATION_JSON_UTF8)),userHander::login)
                .andRoute(POST("/api/user/save").and(contentType(APPLICATION_JSON_UTF8)), userHander::saveUser);


    }
}
