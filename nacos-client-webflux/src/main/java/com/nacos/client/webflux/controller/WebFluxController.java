package com.nacos.client.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author cb
 */
@RestController
public class WebFluxController {

    @Autowired
    private WebClient.Builder webBuilder;
    @RequestMapping("test")
    public Mono<String> testWebFlux(){

        String uri = "http://alibaba-nacos-producer";

        Mono<String> mono = webBuilder.build()
                .get()
                .uri(uri + "/hello?name=" + "Nacos!")
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(mono);
        return mono;


    }
}
