package com.example.videoupload.adapters.controller;

import com.example.videoupload.application.ports.JwtServicePort;
import com.example.videoupload.application.ports.UploadVideoPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class UploadVideoController {


    private final UploadVideoPort uploadVideoPort;

    private final JwtServicePort jwtServicePort;


    public UploadVideoController(UploadVideoPort uploadVideoPort, JwtServicePort jwtServicePort) {
        this.uploadVideoPort = uploadVideoPort;
        this.jwtServicePort = jwtServicePort;
    }

    @PostMapping("/videos")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token)

    {
        try {
            String email = jwtServicePort.validateTokenAndGetEmail(token);
            String fileUrl = uploadVideoPort.uploadVideo(file, email);
            return ResponseEntity.ok("Upload realizado com sucesso! URL: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar o arquivo: " + e.getMessage());
        }
    }
}



