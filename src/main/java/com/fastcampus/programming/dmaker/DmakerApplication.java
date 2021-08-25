package com.fastcampus.programming.dmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@CreateData @LastModifiedDate 특정한 로우가 생기거나 없어질때 손쉽게 값을 변경할 수 있다.
@EnableJpaAuditing
public class DmakerApplication {

    public static void main(String[] args) {

        SpringApplication.run(DmakerApplication.class, args);

    }

}
