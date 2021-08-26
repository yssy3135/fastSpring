package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fastcampus.programming.dmaker.type.DeveloperLevel.*;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    //@Mock 어노테이션이 붙은 Mock 객체를 넣어준다.
    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private final CreateDeveloper.Request defaultRequest = CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    public void testSomething(){
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getAge());


    }
    
    @Test
    public void createDeveloperTest_success() throws Exception {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());


        /**
         * ArgumentCaptor
         * mock객체가 동작을할때 파라미터로 받은 값을 캡쳐해
         * 캡쳐한 값이 검증에 활용할 수 있게 해준다.
         */
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);
        
        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultRequest);


        //then
        /**
         * verify
         * 특정 mock이 몇번 호출되었다는 것을 검증 + repository에 save할때 넘어가는 파라미터를 capture할 수 있다.
         */
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();

        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());


    }
    @Test
    public void createDeveloperTest_failed_with_duplicated() throws Exception {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        /**
         * ArgumentCaptor
         * mock객체가 동작을할때 파라미터로 받은 값을 캡쳐해
         * 캡쳐한 값이 검증에 활용할 수 있게 해준다.
         */
        //when
        //then
        /**
         * assertThrows는 1번째 인자로 예상되는 Exception 클래스,2번째는 해당 Exception을 던지게될 동작
         */
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(defaultRequest)
        );
        assertEquals(DMakerErrorCode.DUPLICATED_MEMER_ID, dMakerException.getDMakerErrorCode());

    }

    

}