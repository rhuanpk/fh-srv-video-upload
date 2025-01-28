package com.example.videoupload.application.ports;

public interface JwtServicePort {
    String validateTokenAndGetEmail(String token);
}
