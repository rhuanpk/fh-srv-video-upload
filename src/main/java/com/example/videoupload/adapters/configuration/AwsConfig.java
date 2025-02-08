package com.example.videoupload.adapters.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class AwsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.s3.sessionToken}")
    private String sessionToken;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public S3AsyncClient s3AsyncClient() {
        logger.info("AWS S3 Access Key: {}", accessKey);
        logger.info("AWS S3 Secret Key: {}", secretKey);
        logger.info("AWS S3 Session Token: {}", sessionToken);
        logger.info("AWS S3 Region: {}", region);

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