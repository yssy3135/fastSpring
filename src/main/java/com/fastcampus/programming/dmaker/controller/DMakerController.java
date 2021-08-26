package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<DeveloperDto> getAllDevelopers() {
        // GET /developers HTTP/1.1"
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ){

        log.info("GET /developers HTTP/1.1");
        return dMakerService.getDeveloperDetail(memberId);
    }


    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {
        // GET /developers HTTP/1.1"
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }


    @PutMapping("developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ){

        log.info("GET /developers HTTP/1.1");
        return dMakerService.editDeveloper(memberId,request);
    }


    @DeleteMapping("developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }


}
