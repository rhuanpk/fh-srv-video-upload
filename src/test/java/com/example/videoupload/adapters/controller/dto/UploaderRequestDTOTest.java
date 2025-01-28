package com.example.videoupload.adapters.controller.dto;

import com.example.videoupload.domain.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UploaderRequestDTOTest {

    @Test
    public void testSetAndGetId() {
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedId = "123e4567-e89b-12d3-a456-426614174000";

        dto.setId(expectedId);
        assertEquals(expectedId, dto.getId(), "ID should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetUrl() {
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedUrl = "https://www.youtube.com/watch?v=123456";

        dto.setUrl(expectedUrl);
        assertEquals(expectedUrl, dto.getUrl(), "URL should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetUsername() {
        UploaderRequestDTO dto = new UploaderRequestDTO();
        String expectedUsername = "user@email.com";

        dto.setUsername(expectedUsername);
        assertEquals(expectedUsername, dto.getUsername(), "Username should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetStatus() {
        UploaderRequestDTO dto = new UploaderRequestDTO();
        VideoStatus expectedStatus = VideoStatus.EM_PROCESSAMENTO;

        dto.setStatus(expectedStatus);
        assertEquals(expectedStatus, dto.getStatus(), "Status should be set and retrieved correctly.");
    }
}