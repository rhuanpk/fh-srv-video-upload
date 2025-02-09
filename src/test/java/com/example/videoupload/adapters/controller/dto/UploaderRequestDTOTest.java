package com.example.videoupload.adapters.controller.dto;

import com.example.videoupload.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UploaderRequestDTOTest {

    @Test
    public void testSetAndGetId() {
        // Criação do DTO
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedId = "123e4567-e89b-12d3-a456-426614174000";

        // Definindo o ID e verificando o getter
        dto.setId(expectedId);
        assertEquals(expectedId, dto.getId(), "ID should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetUrl() {
        // Criação do DTO
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedUrl = "https://www.youtube.com/watch?v=123456";

        // Definindo a URL e verificando o getter
        dto.setUrl(expectedUrl);
        assertEquals(expectedUrl, dto.getUrl(), "URL should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetUsername() {
        // Criação do DTO
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedUsername = "user@email.com";

        // Definindo o username e verificando o getter
        dto.setUsername(expectedUsername);
        assertEquals(expectedUsername, dto.getUsername(), "Username should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetStatus() {
        // Criação do DTO
        UploaderRequestDTO dto = new UploaderRequestDTO();
        VideoStatus expectedStatus = VideoStatus.EM_PROCESSAMENTO;

        // Definindo o status e verificando o getter
        dto.setStatus(expectedStatus);
        assertEquals(expectedStatus, dto.getStatus(), "Status should be set and retrieved correctly.");
    }
}
