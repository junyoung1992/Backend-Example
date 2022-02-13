package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

@Slf4j
public class ComparatorTest {

    @Test
    public void test01() {
        String[] strArr = {"cat", "Dog", "lion", "tiger"};

        Arrays.sort(strArr);    // String 의 Comparable 구현에 의한 정렬
        log.info("strArr={}", Arrays.toString(strArr));

        Arrays.sort(strArr, String.CASE_INSENSITIVE_ORDER); // 대소문자 구분 안함
        log.info("strArr={}", Arrays.toString(strArr));

        Arrays.sort(strArr, new Descending());
        log.info("strArr={}", Arrays.toString(strArr));
    }

    static class Descending implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                Comparable c1 = (Comparable) o1;
                Comparable c2 = (Comparable) o2;

                return c1.compareTo(c2) * -1; // -1을 곱해서 기본 정렬 방식의 역으로 변경한다.
                // return c2.compareTo(c1)
            }
            return -1;
        }
    }

}
