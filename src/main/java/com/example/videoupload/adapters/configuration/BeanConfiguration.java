package com.example.videoupload.adapters.configuration;

import com.example.videoupload.application.service.JwtService;
import com.example.videoupload.application.service.UploadVideo;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UploadVideo uploadVideo(S3AsyncClient s3AsyncClient, RestTemplate restTemplate) {
        return new UploadVideo(s3AsyncClient, restTemplate);
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
