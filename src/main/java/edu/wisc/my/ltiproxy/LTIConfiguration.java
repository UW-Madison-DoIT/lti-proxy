package edu.wisc.my.ltiproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class LTIConfiguration extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(LTIConfiguration.class, args);
    }

}
