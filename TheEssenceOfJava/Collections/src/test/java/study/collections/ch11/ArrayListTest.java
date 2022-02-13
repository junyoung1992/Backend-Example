package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
public class ArrayListTest {

    @Test
    public void test01() {
        ArrayList list1 = new ArrayList(10);
        list1.add(5);
        list1.add(4);
        list1.add(2);
        list1.add(0);
        list1.add(1);
        list1.add(3);

        ArrayList list2 = new ArrayList(list1.subList(1, 4));
        print(list1, list2);

        // list1과 list2를 정렬한다.
        Collections.sort(list1);
        Collections.sort(list2);
        print(list1, list2);

        log.info("list1.containsAll(list2): {}", list1.containsAll(list2));
        
        list2.add("B");
        list2.add("C");
        list2.add("A");
        print(list1, list2);

        list2.set(3, "AA");
        print(list1, list2);

        // list1에서 list2와 겹치는 부분만 남기고 나머지는 삭제한다.
        log.info("list1.retainAll(list2): {}", list1.retainAll(list2));

        // list2에서 list1에 포함된 객체들을 삭제한다.
        for (int i = list2.size() - 1; i >= 0; i--) {
            if(list1.contains(list2.get(i))) {
                list2.remove(i);
            }
        }

        print(list1, list2);
    }

    public static void print(ArrayList<?> list1, ArrayList<?> list2) {
        log.info("list1:{}", list1);
        log.info("list2:{}", list2);
        log.info("");
    }

    @Test
    public void test02() {
        final int LIMIT = 10;
        String source = "0123456789abcdefghijABCDEFGHIJ!@#$%A&*()ZZZ";
        int length = source.length();

        ArrayList list = new ArrayList(length / LIMIT + 10);

        for (int i = 0; i < length; i += LIMIT) {
            if (i + LIMIT < length) {
                list.add(source.substring(i, i + LIMIT));
            } else {
                list.add(source.substring(i));
            }
        }

        for (int i = 0; i < list.size(); i++) {
            log.info("{}", list.get(i));
        }
    }

}
