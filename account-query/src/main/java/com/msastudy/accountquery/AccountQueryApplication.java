package com.msastudy.accountquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.msastudy.coreapi.summary"})
@EnableJpaRepositories({"com.msastudy.coreapi.repo"})
public class AccountQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountQueryApplication.class, args);
    }

}
