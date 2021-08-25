package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 *
 */

//사용자의 값을 받아서 JSON 형식으로 값을 내보내 주겠다.
@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        // GET /developers HTTP/1.1"
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow", "Elsa", "Olaf");
    }


    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {
        // GET /developers HTTP/1.1"
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }


}
