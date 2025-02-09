package com.example.videoupload.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVideoTest {

    @Mock
    private S3AsyncClient s3AsyncClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MultipartFile mockFile;

    @Spy
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
        doNothing().when(uploadVideo).sendVideoStatus(any(), any(), any());

        doReturn("https://buckettest.s3.amazonaws.com/videos/user@example.com/eae105ed-8a12-4326-b71f-7eb777d5856c/video/mp4")
                .when(uploadVideo)
                .generateS3Url(any());

        String result = uploadVideo.uploadVideo(mockFile, "user@example.com");
        assertEquals("https://buckettest.s3.amazonaws.com/videos/user@example.com/eae105ed-8a12-4326-b71f-7eb777d5856c/video/mp4", result);
        verify(uploadVideo, times(1)).sendVideoStatus(any(), any(), any());

    }


    @Test
    void testSendVideoStatus() {

        String id = "eae105ed-8a12-4326-b71f-7eb777d5856c";
        String email = "user@example.com";
        String fileName = "videos/user@example.com/" + id + "/null";
        String expectedUrl = "https://buckettest.s3.amazonaws.com/" + fileName;
        String statusTrackerServiceUrl = "http://mocked-status-service.com";

        ReflectionTestUtils.setField(uploadVideo, "statusTrackerServiceUrl", statusTrackerServiceUrl);

        doReturn(expectedUrl).when(uploadVideo).generateS3Url(fileName);

        when(restTemplate.exchange(
                eq(statusTrackerServiceUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)
        )).thenReturn(ResponseEntity.ok().build());

        uploadVideo.sendVideoStatus(id, email, fileName);

        verify(restTemplate, times(1)).exchange(
                eq(statusTrackerServiceUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }

    @Test
    void testGenerateS3Url() {
        String fileName = "videos/user@example.com/eae105ed-8a12-4326-b71f-7eb777d5856c/test-video.mp4";
        String expectedUrl = "https://buckettest.s3.amazonaws.com/" + fileName;

        doReturn("buckettest").when(uploadVideo).getBucketName();

        String generatedUrl = uploadVideo.generateS3Url(fileName);

        assertEquals(expectedUrl, generatedUrl);
    }

    @Test
    void testUploadVideo_WhenFileIsNull_ShouldThrowException() {
        String email = "user@example.com";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uploadVideo.uploadVideo(null, email);
        });

        assertEquals("File cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testUploadVideo_WhenFileIsEmpty_ShouldThrowException() {
        String email = "user@example.com";

        when(mockFile.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uploadVideo.uploadVideo(mockFile, email);
        });

        assertEquals("File cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testUploadVideo_WhenEmailIsNull_ShouldThrowException() {
        when(mockFile.isEmpty()).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uploadVideo.uploadVideo(mockFile, null);
        });

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testUploadVideo_WhenEmailIsBlank_ShouldThrowException() {
        when(mockFile.isEmpty()).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uploadVideo.uploadVideo(mockFile, "   ");
        });

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testUploadVideoExceptionHandling() throws IOException {

        when(mockFile.getOriginalFilename()).thenReturn("test.mp4");
        when(mockFile.getBytes()).thenReturn(new byte[0]);
        when(mockFile.getSize()).thenReturn(1234L);
        when(mockFile.getContentType()).thenReturn("video/mp4");

        doThrow(new RuntimeException("S3 upload failed")).when(s3AsyncClient)
                .putObject((PutObjectRequest) any(), (AsyncRequestBody) any());

        // Trigger the method and assert that the exception is logged and rethrown
        RuntimeException thrown = null;

        try {
            uploadVideo.uploadVideo(mockFile, "testuser@example.com");
        } catch (RuntimeException e) {
            thrown = e;
        }

        assert thrown != null;
        assert thrown.getMessage().equals("Error during video upload");
    }
}
