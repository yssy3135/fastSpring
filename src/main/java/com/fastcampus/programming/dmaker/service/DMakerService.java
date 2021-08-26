package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.RetiredDeveloper;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DMakerService {

    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
    

    // ACID 특성
    // Atomic
    // Consistency
    // Isolation
    // Durability
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request){
            validateCreateDeveloperRequest(request);

            // business logic start
            Developer developer = Developer.builder()
                    .developerLevel(request.getDeveloperLevel())
                    .developerSkillType(request.getDeveloperSkillType())
                    .experienceYears(request.getExperienceYears())
                    .memberId(request.getMemberId())
                    .statusCode(StatusCode.EMPLOYED)
                    .name(request.getName())
                    .age(request.getAge())
                    .build();

            developerRepository.save(developer);

            return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business validtion
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMER_ID) ;
                }));


    }

    public List<DeveloperDto> getAllEmployedDevelopers() {

        return developerRepository.findDeveloperByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());

    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {

        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request,memberId);
        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(DMakerErrorCode.NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);

    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request,
                                              String memberId) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }

        if (developerLevel == DeveloperLevel.JUNGIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }

        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOTED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);




        // 2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);

        return DeveloperDetailDto.fromEntity(developer);

    }
}
