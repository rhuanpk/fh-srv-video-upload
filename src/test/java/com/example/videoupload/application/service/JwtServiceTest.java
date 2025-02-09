package com.example.videoupload.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Acessa o campo privado 'authServiceUrl' através de Reflection
        Field authUrlField = JwtService.class.getDeclaredField("authServiceUrl");
        authUrlField.setAccessible(true);  // Permite acessar campos privados
        authUrlField.set(jwtService, "http://localhost:8082/user/validateToken?token=");  // Configura o valor desejado
    }

    @Test
    void testValidateTokenAndGetEmail_success() {
        String token = "validToken123";
        String email = "user@example.com";

        Map<String, String> responseBody = Map.of("email", email);
        ResponseEntity<Map<String, String>> mockResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            isNull(),
            any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);

        String result = jwtService.validateTokenAndGetEmail(token);

        assertNotNull(result);
        assertEquals(email, result);

        verify(restTemplate, times(1)).exchange(
            anyString(),
            eq(HttpMethod.GET),
            isNull(),
            any(ParameterizedTypeReference.class)
        );
    }
}
