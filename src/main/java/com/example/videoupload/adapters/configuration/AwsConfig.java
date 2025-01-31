package com.example.videoupload.adapters.configuration;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class AwsConfig {

    private final Dotenv dotenv;

    public AwsConfig() {
        this.dotenv = Dotenv.load();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        String accessKey = dotenv.get("AWS_S3_ACCESS_KEY");
        String secretKey = dotenv.get("AWS_S3_SECRET_KEY");
        String sessionToken = dotenv.get("AWS_S3_SESSION_TOKEN");
        String region = dotenv.get("AWS_S3_REGION");

        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                accessKey,
                secretKey,
                sessionToken
        );

        return S3AsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .region(Region.of(region))
                .build();
    }
}