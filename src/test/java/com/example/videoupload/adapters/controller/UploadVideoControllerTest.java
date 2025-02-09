package com.example.videoupload.adapters.controller;

import com.example.videoupload.application.ports.JwtServicePort;
import com.example.videoupload.application.ports.UploadVideoPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(UploadVideoController.class)
@ActiveProfiles("test")
class UploadVideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UploadVideoPort uploadVideoPort;

    @MockBean
    private JwtServicePort jwtServicePort;

    @Test
    void testUploadVideoSuccess() throws Exception {
        String token = "Bearer validToken";
        String email = "user@example.com";
        String fileUrl = "http://example.com/video.mp4";
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "video content".getBytes());

        when(jwtServicePort.validateTokenAndGetEmail(Mockito.anyString())).thenReturn(email);
        when(uploadVideoPort.uploadVideo(Mockito.any(), Mockito.anyString())).thenReturn(fileUrl);

        mockMvc.perform(multipart("/uploads/videos")
                        .file(file)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
