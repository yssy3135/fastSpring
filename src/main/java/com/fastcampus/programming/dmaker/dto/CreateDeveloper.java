package com.fastcampus.programming.dmaker.dto;

import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        private Integer experienceYears;
        @NotNull
        @Size(min = 3 , max = 50, message = "memberId size must 3~50")
        private String memberId;

        @NotNull
        @Size(min = 3 , max = 20, message = "name size must 3~50")
        private String name;

        @Min(18)
        private Integer age;

    }
}
