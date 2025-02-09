package com.example.videoupload.application.service;

import com.example.videoupload.adapters.controller.dto.UploaderRequestDTO;
import com.example.videoupload.application.ports.UploadVideoPort;
import com.example.videoupload.domain.enums.VideoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadVideo implements UploadVideoPort {

    private final S3AsyncClient s3AsyncClient;

    private final RestTemplate restTemplate;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${url.status.tracker.service}")
    private String statusTrackerServiceUrl;

    private static final Logger log = LoggerFactory.getLogger(UploadVideo.class);

    public UploadVideo(S3AsyncClient s3AsyncClient, RestTemplate restTemplate) {
        this.s3AsyncClient = s3AsyncClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public String uploadVideo(MultipartFile file, String email) throws IOException {
        validateParameters(file, email);

        String id = UUID.randomUUID().toString();
        String fileName = buildFilePath(email, id, file);
        Path tempFile = createTemporaryFile(file, id);

        try {
            log.info("Starting upload for file: {}", fileName);
            log.info("Temporary file created at: {}", tempFile.toString());
            log.info("File size: {} bytes", file.getSize());

            uploadToS3(file, tempFile, fileName, id, email);

            sendVideoStatus(id, email, fileName);

            return generateS3Url(fileName);
        } catch (Exception e) {
            log.error("Error during video upload: {}", e.getMessage(), e);
            throw new RuntimeException("Error during video upload", e);
        } finally {
            deleteTemporaryFile(tempFile);
        }
    }

    private void validateParameters(MultipartFile file, String email) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
    }

    private String buildFilePath(String email, String id, MultipartFile file) {
        return String.format("videos/%s/%s/%s", email, id, file.getOriginalFilename());
    }

    @SuppressWarnings("squid:S5443")
    private Path createTemporaryFile(MultipartFile file, String id) throws IOException {
        Path tempFile = Files.createTempFile(id, file.getOriginalFilename());
        Files.write(tempFile, file.getBytes());
        return tempFile;
    }

    private void uploadToS3(MultipartFile file, Path tempFile, String fileName, String id, String email) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("id", id);
        metadata.put("email", email);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(getBucketName())
                .key(fileName)
                .contentType(file.getContentType())
                .metadata(metadata)
                .build();

        s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromFile(tempFile))
                .whenComplete((response, exception) -> {
                    if (exception != null) {
                        log.error("Error during S3 upload: {}", exception.getMessage(), exception);
                    } else {
                        log.info("Upload successful: {}", response);
                        log.info("ETag: {}", response.eTag());
                    }
                })
                .join();
    }

    public void sendVideoStatus(String id, String email, String fileName) {
        UploaderRequestDTO request = new UploaderRequestDTO();
        request.setId(id);
        request.setUsername(email);
        request.setUrl(generateS3Url(fileName));
        request.setStatus(VideoStatus.RECEBIDO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UploaderRequestDTO> entity = new HttpEntity<>(request, headers);

        String url = statusTrackerServiceUrl;

        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    public String generateS3Url(String fileName) {
        return "https://" + getBucketName() + ".s3.amazonaws.com/" + fileName;
    }

    public String getBucketName() {
        return bucketName;
    }

    private void deleteTemporaryFile(Path tempFile) {
        try {
            Files.delete(tempFile);
        } catch (IOException e) {
            log.warn("Failed to delete temporary file: {}", e.getMessage());
        }
    }
}
