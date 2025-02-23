package com.minbak.web.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/portone")
public class APIPortOneController {

    @Autowired
    WebClient webClient; //http요청 객체

    @Value("${PORTONE_API_KEY}")
    private String apiKey;

    @Value("${PORTONE_API_SECRET}")
    private String apiSecret;

    @PostMapping("/get-token")
    public Mono<ResponseEntity<Map>> getAccessToken() {
        // 포트원 토큰 요청 URL
        return webClient.post()
                .uri("https://api.iamport.kr/users/getToken") //토큰 요청 url
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("imp_key", apiKey, "imp_secret", apiSecret))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve access token"))));
    }

}
