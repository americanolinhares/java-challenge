package com.aubay.challenge.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AubayApplication {

  public static void main(String[] args) {

    SpringApplication.run(AubayApplication.class, args);
  }
}
