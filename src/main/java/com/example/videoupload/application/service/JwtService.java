package com.example.videoupload.application.service;

import com.example.videoupload.application.ports.JwtServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

@Service
public class JwtService implements JwtServicePort {

    private final RestTemplate restTemplate;

    @Value("${url.auth.service}")
    private String authServiceUrl;

    public JwtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String validateTokenAndGetEmail(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String url = authServiceUrl + token;

        try {
            ResponseEntity<Map<String, String>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            // Verificar se a resposta é OK e o corpo não é nulo
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, String> responseBody = response.getBody();

                // Verifique se responseBody não é nulo antes de acessar
                if (responseBody != null && responseBody.containsKey("email")) {
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