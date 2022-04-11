package me.whiteship.inflearnthejavatest;

import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.domain.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@ExtendWith(FindSlowTestExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @SlowTest
    @DisplayName("스터디 만들기 \uD83D\uDE31")
    void create_new_study() throws InterruptedException {
        Thread.sleep(1000L);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());

        Study study = new Study(10);

        assertAll(
                () ->assertNotNull(study),
//                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다.")
                () ->assertEquals(StudyStatus.DRAFT, study.getStatus(),
                    () -> "스터디를 처음 만들면" + StudyStatus.DRAFT + " 상태다"),
                () ->assertTrue(study.getLimitCount() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );

        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(10);
        });

        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(10);
        }); // 다른 쓰레드를 생성해서 동작하기 때문에 예상치 못한 동작이 발생할 수 있음에 주의해야 함
        // TODO ThreadLocal
    }

    // @Ignored JUnit4
    // @Disabled
    @FastTest
    @DisplayName("스터디 만들기 (❁ᴗ͈ˬᴗ͈)")
    void create_new_study_again() {
        System.out.println("create1");
    }

    @Test
    void testCondition() {
//        String test_env = System.getenv("TEST_ENV");
        String test_env = "LOCAL";
        System.out.println(test_env);

        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
            System.out.println("LOCAL");
            Study actual = new Study(10);
            assertThat(actual.getLimitCount()).isGreaterThan(0);
        });
    }

    @RepeatedTest(10)
    void repeatTest() {
        System.out.println("test");
    }

    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest2(RepetitionInfo repetitionInfo) {
        System.out.println("test " +
                repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    // only JUnit5
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요"})
    // @EmptySource
    // @NullSource
    @NullAndEmptySource
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 30})
    void parameterizedTest2(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimitCount());
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTest3(Integer limit, String name) {
        System.out.println(new Study(limit, name));
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTest4(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }



    // @BeforeClass JUnit4
    @BeforeAll  // 모든 테스트가 실행되기 이전에 한 번 실행됨
    static void beforeAll() {
        System.out.println("before all");
    }

    // @AfterClass  Junit4
    @AfterAll   // 모든 테스트가 실행된 이후 한 번 실행됨
    static void afterAll() {
        System.out.println("after all");
    }

    // @Before  JUnit4
    @BeforeEach // 각 테스트가 실행되기 이전에 실행됨
    void beforeEach() {
        System.out.println("before each");
    }

    // @After   JUnit
    @AfterEach  // 각 테스트가 실행된 후 실행됨
    void afterEach() {
        System.out.println("after each");
    }

}