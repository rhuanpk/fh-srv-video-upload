package com.example.videoupload.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.async.AsyncRequestBody;

@ExtendWith(MockitoExtension.class)
class UploadVideoTest {

    @Mock
    private S3AsyncClient s3AsyncClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private UploadVideo uploadVideo;

    @BeforeEach
    void setUp() {
        lenient().when(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
                .thenReturn(CompletableFuture.completedFuture(PutObjectResponse.builder().build()));
    }

    @Test
    void testUploadVideo_success() throws Exception {
        when(mockFile.getSize()).thenReturn(1000L);
        when(mockFile.getContentType()).thenReturn("video/mp4");
        when(mockFile.getBytes()).thenReturn("video content".getBytes());

        uploadVideo.uploadVideo(mockFile, "user@example.com");
    }
}
