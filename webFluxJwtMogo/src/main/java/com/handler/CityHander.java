package com.handler;

import com.entity.City;
import com.repository.CityRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
public class CityHander {
    private final CityRepository cityRepository;
    public CityHander(CityRepository cityRepository){
        this.cityRepository=cityRepository;
    }
    public Mono<ServerResponse> saveCity(ServerRequest request){
        Mono<City> cityMono = request.bodyToMono(City.class).flatMap(city -> cityRepository.save(city));
        return  ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(cityMono, City.class);
    }
    public Mono<ServerResponse> listCity(ServerRequest request){
        Flux<City> cityFlux = cityRepository.findAll();
        return ok().contentType(APPLICATION_JSON_UTF8).body(cityFlux, City.class);
    }
    public Mono<ServerResponse> getCityDetail(ServerRequest request){
        Long id=Long.valueOf( request.pathVariable("id"));
        return cityRepository.findById(id)
                .flatMap(city ->ok().contentType(APPLICATION_JSON_UTF8).body(fromObject(city)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }
    public Mono<ServerResponse> deleteCity(ServerRequest request){
         Long id=Long.valueOf( request.pathVariable("id"));
         return  ok().build(cityRepository.deleteById(id).then());
    }
}
