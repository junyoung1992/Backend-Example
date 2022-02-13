package study.oop.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CardTest {

    @Test
    public void test01() {
        log.info("Card.width={}", Card.width);
        log.info("Card.height={}", Card.height);

        Card c1 = new Card();
        c1.kind = "Heart";
        c1.number = 7;

        Card c2 = new Card();
        c2.kind = "Spade";
        c2.number = 4;

        log.info("c1은 {}, {}, 이며, 크기는 ({}, {})", c1.kind, c1.number, c1.width, c1.height);
        log.info("c2는 {}, {}, 이며, 크기는 ({}, {})", c2.kind, c2.number, c2.width, c2.height);

        log.info("c1의 width와 height를 각각 50, 80으로 변경합니다.");
        c1.width = 50;
        c1.height = 80;

        log.info("c1은 {}, {}, 이며, 크기는 ({}, {})", c1.kind, c1.number, c1.width, c1.height);
        log.info("c2는 {}, {}, 이며, 크기는 ({}, {})", c2.kind, c2.number, c2.width, c2.height);
    }

}