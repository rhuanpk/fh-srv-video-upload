package com.example.videoupload.adapters.controller;

import com.example.videoupload.application.ports.JwtServicePort;
import com.example.videoupload.application.ports.UploadVideoPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UploadVideoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UploadVideoPort uploadVideoPort;

    @Mock
    private JwtServicePort jwtServicePort;

    @InjectMocks
    private UploadVideoController uploadVideoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(uploadVideoController).build();
    }

    @Test
    void testUploadVideoSuccess() throws Exception {
        String token = "Bearer validToken";
        String email = "user@example.com";
        String fileUrl = "http://example.com/video.mp4";
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "video content".getBytes());

        when(jwtServicePort.validateTokenAndGetEmail(token)).thenReturn(email);
        when(uploadVideoPort.uploadVideo(file, email)).thenReturn(fileUrl);

        mockMvc.perform(multipart("/uploads/videos")
                        .file(file)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}