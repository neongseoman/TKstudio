package com.ssafy.gallery.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsS3Config {
//    @Value("${cloud.aws.credentials.access-key}")
    private final String accessKey = System.getenv("AWS_ACCESS_KEY");
//    @Value("${cloud.aws.credentials.secret-key}")
    private final String secretKey= System.getenv("AWS_SECRET_KEY");
//    @Value("${cloud.aws.region.static}")
    private final String region =  System.getenv("AWS_REGION");

    @Bean
    @Primary
    public BasicAWSCredentials awsCredentialsProvider() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return basicAWSCredentials;
    }

    @Bean
    public AmazonS3 amazonS3() {
        System.out.println(region.charAt(1));
        AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
                .build();
        return s3Builder;
    }

}