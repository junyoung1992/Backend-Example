package study.oop.ch6;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyMath {

    long add(long a, long b) {
        long result = a + b;
        return result;
    }

    long subtract (long a, long b) {
        return a - b;
    }

    long multiply(long a, long b) {
        return a * b;
    }

    double divide(double a, double b) {
        if (b == 0) {
            log.info("0으로 나눌 수 없습니다.");
            return 0;
        }
        return a / b;
    }

}
