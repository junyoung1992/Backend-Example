package study.oop.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TvTest {

    @Test
    public void test01() {
        Tv t;
        t = new Tv();
        t.channel = 7;
        t.channelDown();
        log.info("현재 채널은 {} 입니다.", t.channel);
    }

    @Test
    public void test02() {
        Tv t1 = new Tv();
        Tv t2 = new Tv();
        log.info("t1의 channel 값은 {} 입니다.", t1.channel);
        log.info("t2의 channel 값은 {} 입니다.", t2.channel);

        t1.channel = 7;
        log.info("t1의 channel 값을 7로 변경하였습니다.");

        log.info("t1의 channel 값은 {} 입니다.", t1.channel);
        log.info("t2의 channel 값은 {} 입니다.", t2.channel);
    }

    @Test
    public void test03() {
        Tv t1 = new Tv();
        Tv t2 = new Tv();

        t2 = t1;    // t1이 저장하고 있는 값(주소)를 t2에 저장한다.
        t1.channel = 7;
        log.info("t1의 channel 값을 7로 변경하였습니다.");

        log.info("t1의 channel 값은 {} 입니다.", t1.channel);
        log.info("t2의 channel 값은 {} 입니다.", t2.channel);
    }

    /**
     * 객체 배열
     */
    @Test
    public void test04() {
        Tv[] tvArr = new Tv[3];

        for (int i = 0; i < tvArr.length; i++) {
            tvArr[i] = new Tv();
            tvArr[i].channel = i + 10;
        }

        for (int i = 0; i < tvArr.length; i++) {
            tvArr[i].channelUp();
            log.info("tvArr{}.channel={}", i, tvArr[i].channel);
        }
    }

}