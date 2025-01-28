package com.example.videoupload.application.service;

import com.example.videoupload.application.ports.JwtServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class JwtService implements JwtServicePort {

    @Value("${auth.url}")
    private String authUrl;

    @Override
    public String validateTokenAndGetEmail(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = authUrl + token;

        try {

            ResponseEntity<Map<String, String>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, String> responseBody = response.getBody();

                if (responseBody.containsKey("email")) {
                    return responseBody.get("email");
                } else {
                    throw new IllegalArgumentException("Token inválido ou email não encontrado.");
                }
            } else {
                throw new IllegalArgumentException("Falha na validação do token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar o token: " + e.getMessage(), e);
        }
    }

}
