package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    /**
     * memberService    @Transactional: OFF
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON
     */
    @Test
    public void outerTxOff_success() {
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1(username);

        // then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isPresent()).isTrue();
    }

    /**
     * memberService    @Transactional: OFF
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON
     */
    @Test
    public void outerTxOff_fail() {
        // given
        String username = "로그예외_outerTxOff_success";

        // when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        // then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isEmpty()).isTrue();
    }

    /**
     * memberService    @Transactional: ON
     * memberRepository @Transactional: OFF
     * logRepository    @Transactional: OFF
     */
    @Test
    public void singleTx() {
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1(username);

        // then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isPresent()).isTrue();
    }

    /**
     * memberService    @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON
     */
    @Test
    public void outerTxOn_success() {
        // given
        String username = "outerTxOn_success";

        // when
        memberService.joinV1(username);

        // then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isPresent()).isTrue();
    }

    /**
     * memberService    @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON
     */
    @Test
    public void outerTxOn_fail() {
        // given
        String username = "로그예외_outerTxOn_success";

        // when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        // then
        assertThat(memberRepository.find(username).isEmpty()).isTrue();
        assertThat(logRepository.find(username).isEmpty()).isTrue();
    }

    /**
     * memberService    @Transactional: OM
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON(REQUITES_NEW) Exception
     */
    @Test
    public void recoverException_success() {
        // given
        String username = "로그예외_recoverException_success";

        // when
        memberService.joinV2(username);

        // then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isEmpty()).isTrue();
    }

    /**
     * memberService    @Transactional: ON
     * memberRepository @Transactional: ON
     * logRepository    @Transactional: ON Exception
     */
    @Test
    public void recoverException_fail() {
        // given
        String username = "로그예외_recoverException_success";

        // when
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        // then
        assertThat(memberRepository.find(username).isEmpty()).isTrue();
        assertThat(logRepository.find(username).isEmpty()).isTrue();
    }

}