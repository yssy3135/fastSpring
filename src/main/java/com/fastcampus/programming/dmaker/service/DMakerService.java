package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DMakerService {

    private final DeveloperRepository developerRepository;
    private final EntityManager em;

    

    // ACID 특성
    // Atomic
    // Consistency
    // Isolation
    // Durability
    @Transactional
    public void createDeveloper(CreateDeveloper.Request request){
            validateCreateDeveloperRequest(request);

            // business logic start
            Developer developer = Developer.builder()
                    .developerLevel(DeveloperLevel.JUNGIOR)
                    .developerSkillType(DeveloperSkillType.FRONT_END)
                    .experienceYears(2)
                    .name("Olaf")
                    .age(5)
                    .build();

                    developerRepository.save(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business validtion
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();

        if(developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }

        if(developerLevel == DeveloperLevel.JUNIOR
        && (experienceYears < 4 || experienceYears > 10)){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }

        if(developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4 ){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCED);
        }

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMER_ID) ;
                }));


    }

}
