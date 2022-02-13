package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ArrayListLinkedListTest {

    @Test
    public void test01() {
        // 추가할 데이터의 개수를 고려해서 충분히 잡아야 한다.
        ArrayList al = new ArrayList(2000000);
        LinkedList ll = new LinkedList();

        log.info("= 순차적으로 추가하기 =");
        log.info("ArrayList:{}", add1(al));
        log.info("LinkedList:{}", add1(ll));
        log.info("");

        log.info("= 중간에서 추가하기 =");
        log.info("ArrayList:{}", add2(al));
        log.info("LinkedList:{}", add2(ll));
        log.info("");

        log.info("= 중간에서 삭제하기 =");
        log.info("ArrayList:{}", remove2(al));
        log.info("LinkedList:{}", remove2(ll));
        log.info("");

        log.info("= 순차적으로 삭제하기 =");
        log.info("ArrayList:{}", remove1(al));
        log.info("LinkedList:{}", remove1(ll));
        log.info("");
    }

    public static long add1(List list) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            list.add(i + "");
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long add2(List list) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            list.add(500, "X");
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long remove1(List list) {
        long start = System.currentTimeMillis();
        for (int i = list.size() - 1; i >= 0; i--) {
            list.remove(i);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long remove2(List list) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            list.remove(i);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    @Test
    public void test02() {
        ArrayList al = new ArrayList(1000000);
        LinkedList ll = new LinkedList();
        add(al);
        add(ll);

        log.info("= 접근시간 테스트 =");
        log.info("ArrayList:{}", access(al));
        log.info("LinkedList:{}", access(ll));
    }

    public static void add(List list) {
        for (int i = 0; i < 1000000; i++) {
            list.add(i + "");
        }
    }
    
    public static long access(List list) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            list.get(i);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

}
