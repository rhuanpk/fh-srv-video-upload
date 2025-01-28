package com.example.videoupload.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws Exception {

        Field authUrlField = JwtService.class.getDeclaredField("authUrl");
        authUrlField.setAccessible(true);
        authUrlField.set(jwtService, "http://localhost:8082/user/validateToken?token=");
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