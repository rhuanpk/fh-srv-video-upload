package com.example.videoupload.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UploadVideoTest {

    @Mock
    private S3AsyncClient s3AsyncClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private UploadVideo uploadVideo;

    @Test
    public void testUploadVideo_success() throws IOException {

        String email = "user@example.com";

        when(file.getOriginalFilename()).thenReturn("video.mp4");
        when(file.getContentType()).thenReturn("video/mp4");


        PutObjectResponse putObjectResponse = mock(PutObjectResponse.class);
        when(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
                .thenReturn(completedFuture(putObjectResponse));


        ResponseEntity<Void> responseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);


        String s3Url = uploadVideo.uploadVideo(file, email);


        assertNotNull(s3Url);
        assertTrue(s3Url.contains("https://"));
        assertTrue(s3Url.contains("s3.amazonaws.com"));


        verify(s3AsyncClient, times(1)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
    }


    @Test
    public void testUploadVideoWithoutNameThenThrow() throws IOException {

        String email = "user@example.com";

        assertThrows(IllegalArgumentException.class, () -> uploadVideo.uploadVideo(null, email));

    }

    @Test
    public void testUploadVideoWithoutEmailThenThrow() throws IOException {

        when(file.getOriginalFilename()).thenReturn("video.mp4");
        when(file.getContentType()).thenReturn("video/mp4");

        assertThrows(IllegalArgumentException.class, () -> uploadVideo.uploadVideo(file, null));

    }
}