package com.movies.config.dynamo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = "com.movies.repository")
public class DynamoDBConfig {

    @Value("${amazon.aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Value("${amazon.aws.access-key}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secret-key}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withEndpointConfiguration(getEndpointConfiguration(amazonDynamoDBEndpoint))
                .build();
    }
    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String url) {
        return new AwsClientBuilder.EndpointConfiguration(url, amazonDynamoDBRegion);
    }
    private AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(getBasicAWSCredentials());
    }

    private BasicAWSCredentials getBasicAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}

