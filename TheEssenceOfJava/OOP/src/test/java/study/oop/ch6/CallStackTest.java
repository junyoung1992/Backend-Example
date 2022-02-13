package study.oop.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CallStackTest {

    @Test
    public void test01() {
        log.info("test01() 이 시작되었음");
        firstMethod();
        log.info("test01() 이 끝났음");
    }

    static void firstMethod() {
        log.info("firstMethod() 이 시작되었음");
        secondMethod();
        log.info("firstMethod() 이 끝났음");
    }

    static void secondMethod() {
        log.info("secondMethod() 이 시작되었음");
        log.info("secondMethod() 이 끝났음");
    }

}
