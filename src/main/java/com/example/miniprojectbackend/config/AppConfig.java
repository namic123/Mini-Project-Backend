package com.example.miniprojectbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AppConfig {
    // aws s3 접근을 위한 엑세스 키 id
    @Value("${aws.accessKeyId}")
    private String accessKeyId;
    // 시크릿 엑세스 키
    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;
    @Bean
    public S3Client s3Client(){
        // AWS 자격 증명 생성
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        // 생성된 자격 증명을 통해 AwsCredentialsProvider 객체 생성
        // 이 객체는 S3클라이언트가 AWS 서비스에 안전하게 접근할 수 있도록 자격 증명을 제공
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        // S3 클라이언트의 빌더 생성
        S3Client s3 = S3Client.builder()
                // 클라이언트의 국가 설정
                .region(Region.AP_NORTHEAST_2)
                // 앞서 생성한 자격 증명 제공자를 클라이언트에 설정
                .credentialsProvider(provider)
                // 최종적으로 구성된 S3 클라이언트 인스턴스 생성
                .build();
        return s3;
    }
}
