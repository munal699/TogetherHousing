package org.example.togetherhousing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@org.springframework.scheduling.annotation.EnableAsync
@SpringBootApplication
public class TogetherHousingApplication {

    public static void main(String[] args) {

        SpringApplication.run(TogetherHousingApplication.class, args);
    }

}
