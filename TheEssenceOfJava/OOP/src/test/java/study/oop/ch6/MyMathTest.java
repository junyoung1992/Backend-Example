package study.oop.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MyMathTest {

    @Test
    public void test01() {
        MyMath mm = new MyMath();
        long result1 = mm.add(5L, 3L);
        long result2 = mm.subtract(5L, 3L);
        long result3 = mm.multiply(5L, 3L);
        double result4 = mm.divide(5L, 3L);

        log.info("add(5L, 3L)={}", result1);
        log.info("subtract(5L, 3L)={}", result2);
        log.info("multiply(5L, 3L)={}", result3);
        log.info("divide(5L, 3L)={}", result4);
    }

}