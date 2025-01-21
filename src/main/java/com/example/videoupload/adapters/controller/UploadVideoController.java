package com.example.videoupload.adapters.controller;

import com.example.videoupload.application.ports.UploadVideoPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class UploadVideoController {


    private final UploadVideoPort uploadVideoPort;


    public UploadVideoController(UploadVideoPort uploadVideoPort) {
        this.uploadVideoPort = uploadVideoPort;
    }

    @PostMapping("/videos")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email) {
        try {
            String fileUrl = uploadVideoPort.uploadVideo(file, email);
            return ResponseEntity.ok("Upload realizado com sucesso! URL: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar o arquivo: " + e.getMessage());
        }
    }
}



