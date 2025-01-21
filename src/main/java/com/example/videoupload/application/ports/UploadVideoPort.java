package com.example.videoupload.application.ports;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface UploadVideoPort {

    String uploadVideo(MultipartFile file, String email) throws IOException, InterruptedException, ExecutionException;
}
