package com.birthdaymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BirthdayManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BirthdayManagerApplication.class, args);
    }
}
