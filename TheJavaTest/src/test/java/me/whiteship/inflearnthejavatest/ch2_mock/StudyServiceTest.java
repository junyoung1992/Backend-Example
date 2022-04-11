package me.whiteship.inflearnthejavatest.ch2_mock;

import me.whiteship.inflearnthejavatest.domain.Member;
import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.domain.StudyStatus;
import me.whiteship.inflearnthejavatest.member.MemberService;
import me.whiteship.inflearnthejavatest.study.StudyRepository;
import me.whiteship.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    // @Mock 애너테이션으로 간단히 Mock 객체를 생성할 수 있다.
//    @Mock MemberService memberService;
//    @Mock StudyRepository studyRepository;

    @Test
    @DisplayName("Mock 객체 생성")
    void createStudyService(// 파라미터에 Mock 객체를 정의하면 해당 메서드에서만 생성된다.
                            @Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
//        MemberService memberService = Mockito.mock(MemberService.class);
//        StudyRepository studyRepository = Mockito.mock(StudyRepository.class);
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }

    /**
     * Mock 객체를 조작해서 특정한 매개변수를 받은 경우 특정한 값을 리턴하거나 예외를 던지게 만들 수 있다.
     * @param memberService
     * @param studyRepository
     */
    @Test
    @DisplayName("Mock 객체 Stubbing")
    void mockStubbing(@Mock MemberService memberService,
                      @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");
//        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(memberService.findById(any())).thenReturn(Optional.of(member));    // 어떤 값이 들어와도 Optional.of(member) 리턴

        assertEquals("orsay0827@gmail.com", memberService.findById(1L).get().getEmail());
        assertEquals("orsay0827@gmail.com", memberService.findById(2L).get().getEmail());

        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        assertThrows(IllegalArgumentException.class, () -> memberService.validate(1L));
        memberService.validate(2L);

        Study study = new Study(10, "java");
        studyService.createNewStudy(1L, study);
    }

    /**
     * Mock 객체를 조작해서 특정한 매개변수를 받은 경우 특정한 값을 리턴하거나 예외를 던지게 만들 수 있다.
     * @param memberService
     * @param studyRepository
     */
    @Test
    @DisplayName("호출 순서에 따라 Stubbing이 다르게 동작할 수 있다")
    void mockStubbing2(@Mock MemberService memberService,
                      @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");

        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))    // 첫 번째 호출
                .thenThrow(new RuntimeException())  // 두 번째 호출
                .thenReturn(Optional.empty());      // 세 번째 호출

        assertEquals("orsay0827@gmail.com", memberService.findById(1L).get().getEmail());
        assertThrows(RuntimeException.class, () -> memberService.findById(2L));
        assertEquals(Optional.empty(), memberService.findById(1L));
    }

    @Test
    @DisplayName("연습문제 1")
    public void example1(@Mock MemberService memberService,
                         @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");

        Study study = new Study(10, "테스트");

        // TODO memberService 객체에 findById 메서드를 1L 값으로 호출하면 Optional.of(member) 객체를 리턴하도록 Stubbing
        when(memberService.findById(1L))
                .thenReturn(Optional.of(member));

        // TODO studyRepository 객체에 save 메서드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        when(studyRepository.save(study))
                .thenReturn(study);

        studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwnerId());
        assertEquals(member.getId(), study.getOwnerId());
    }

    @Test
    @DisplayName("Mock 객체 확인")
    public void createNewStudy(@Mock MemberService memberService,
                               @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");

        Study study = new Study(10, "테스트");

        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwnerId());

        // memberService에서 notify라는 메서드가 study라는 파라미터를 가지고 한 번 호출되었어야 한다.
        verify(memberService, times(1)).notify(study);
//        verify(memberService, times(1)).notify(member);
//        verify(memberService, times(1)).notify(any());

        // memberService에서 validate라는 메서드는 한 번도 호출되지 않았다.
        verify(memberService, never()).validate(any());

        // memberService에서 notify(study) 이후 notify(member)가 실행되어야 한다.
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
//        inOrder.verify(memberService).notify(member);

        // memberService는 더이상 interaction이 없어야 한다.
        verifyNoMoreInteractions(memberService);
    }

    @Test
    void behaviorDrivenDesign(@Mock MemberService memberService,
                              @Mock StudyRepository studyRepository) {
        // given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("orsay0827@gmail.com");

        Study study = new Study(10, "테스트");

        // Mockito when -> given
//        when(memberService.findById(1L)).thenReturn(Optional.of(member));
//        when(studyRepository.save(study)).thenReturn(study);
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // when
        studyService.createNewStudy(1L, study);

        // then
        assertEquals(member.getId(), study.getOwnerId());

        // Mockito: verify -> then
//        verify(memberService, times(1)).notify(study);
//        verifyNoMoreInteractions(memberService);
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("연습문제 2")
    public void example2(@Mock MemberService memberService,
                         @Mock StudyRepository studyRepository) {
        // given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");

        // TODO studyRepository Mock 객체의 save 메서드 호출시 study를 리턴하도록 만들기
        given(studyRepository.save(study)).willReturn(study);

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

}