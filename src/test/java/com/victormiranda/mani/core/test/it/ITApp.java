package com.victormiranda.mani.core.test.it;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@ActiveProfiles("test")
@ComponentScan(value = "com.victormiranda.mani.core")
@TestPropertySource(locations="classpath:application-test.properties")
public class ITApp {
    public static void main(String[] args) {
        SpringApplication.run(ITApp.class, args);
    }

}
