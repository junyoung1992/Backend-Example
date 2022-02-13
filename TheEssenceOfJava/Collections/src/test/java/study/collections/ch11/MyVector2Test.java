package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MyVector2Test {

    @Test
    public void test01() {
        MyVector2 v = new MyVector2();
        v.add("0");
        v.add("1");
        v.add("2");
        v.add("3");
        v.add("4");

        log.info("삭제 전: {}", v);
        Iterator it = v.iterator();
        it.next();
        it.remove();
        it.next();
        it.remove();
        log.info("삭제 후: {}", v);
    }

}