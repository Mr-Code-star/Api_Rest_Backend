package com.sanuvi.ferova.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class ApiRestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiRestBackendApplication.class, args);
    }

}
