package me.whiteship.inflearnthejavatest.ch3_testcontainer;

import lombok.extern.slf4j.Slf4j;
import me.whiteship.inflearnthejavatest.domain.Member;
import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.domain.StudyStatus;
import me.whiteship.inflearnthejavatest.member.MemberService;
import me.whiteship.inflearnthejavatest.study.StudyRepository;
import me.whiteship.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = DockerComposeTest.ContainerPropertyInitializer.class)
class DockerComposeTest {

    @Mock MemberService memberService;
    @Autowired StudyRepository studyRepository;

    @Value("${container.port}") int port;

    @Container
    static DockerComposeContainer<?> composeContainer =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("study-db", 3306);

    @BeforeEach
    void beforeEach() {
        System.out.println("PORT: " + port);
    }

    @Test
    public void createNewStudy() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");

        Study study = new Study(10, "test");

        // given
        given(memberService.findById(1L)).willReturn(Optional.of(member));

        // when
        studyService.createNewStudy(1L, study);

        // then
        assertEquals(member.getId(), study.getOwnerId());
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    public void openStudy() {
        // given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "The Java, Test");
        assertNull(study.getOpenedDateTime());

        // when
        studyService.openStudy(study);

        // then
        // TODO study의 status가 OPENED로 변경되었는지 확인
        assertEquals(StudyStatus.OPENED, study.getStatus());
        // TODO study의 openedDateTime이 null이 아닌지 확인
        assertNotNull(study.getOpenedDateTime());
        // TODO memberService의 nofity(study)가 호출되었는지 확인
        then(memberService).should().notify(study);
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" + composeContainer.getServicePort("study-db", 3306))
                    .applyTo(context.getEnvironment());
        }

    }

}