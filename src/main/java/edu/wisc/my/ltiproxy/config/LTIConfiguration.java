package edu.wisc.my.ltiproxy.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class LTIConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(LTIConfiguration.class, args);
    }
}
