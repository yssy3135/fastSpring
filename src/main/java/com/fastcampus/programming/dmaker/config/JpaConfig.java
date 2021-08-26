package com.fastcampus.programming.dmaker.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration

//@CreateData @LastModifiedDate 특정한 로우가 생기거나 없어질때 손쉽게 값을 변경할 수 있다.
@EnableJpaAuditing
public class JpaConfig {

}
