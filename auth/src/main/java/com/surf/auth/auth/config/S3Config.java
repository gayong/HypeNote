package com.surf.auth.auth.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;

    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

    @Value("${cloud.aws.region.static}")
    private String REGION;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
