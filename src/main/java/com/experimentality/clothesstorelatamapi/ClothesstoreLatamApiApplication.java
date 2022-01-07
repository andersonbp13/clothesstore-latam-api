package com.experimentality.clothesstorelatamapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.experimentality")
public class ClothesstoreLatamApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothesstoreLatamApiApplication.class, args);
    }

}
