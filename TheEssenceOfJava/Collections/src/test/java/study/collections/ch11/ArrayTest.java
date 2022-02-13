package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@Slf4j
public class ArrayTest {

    @Test
    public void test01() {
        int[] arr = {0, 1, 2, 3, 4};
        int[][] arr2D = {{11, 12, 13}, {21, 22, 23}};
        log.info("arr={}", Arrays.toString(arr));
        log.info("arr2D={}", Arrays.deepToString(arr2D));

        int[] arr2 = Arrays.copyOf(arr, arr.length);
        int[] arr3 = Arrays.copyOf(arr, 3);
        int[] arr4 = Arrays.copyOf(arr, 7);
        int[] arr5 = Arrays.copyOfRange(arr, 2, 4);
        int[] arr6 = Arrays.copyOfRange(arr, 0, 7);

        log.info("arr2={}", Arrays.toString(arr2));
        log.info("arr3={}", Arrays.toString(arr3));
        log.info("arr4={}", Arrays.toString(arr4));
        log.info("arr5={}", Arrays.toString(arr5));
        log.info("arr6={}", Arrays.toString(arr6));

        int[] arr7 = new int[5];
        Arrays.fill(arr7, 9);
        log.info("arr7={}", arr7);

        Arrays.setAll(arr7, i -> (int) (Math.random() * 6) + 1);
        log.info("arr7={}", Arrays.toString(arr7));

        for (int i: arr7) {
            char[] graph = new char[i];
            Arrays.fill(graph, '*');
            log.info(new String(graph) + i);
        }

        String[][] str2D = new String[][]{{"aaa", "bbb"}, {"AAA", "BBB"}};
        String[][] str2D2 = new String[][]{{"aaa", "bbb"}, {"AAA", "BBB"}};

        // equals는 1차원 배열만 비교 가능.
        // 다차원 배열을 비교할 경우 배열의 주소를 비교함.
        log.info("{}", Arrays.equals(str2D, str2D2));
        log.info("{}", Arrays.deepEquals(str2D, str2D2));

        char[] chArr = {'A', 'D', 'C', 'B', 'E'};
        log.info("chArr={}", Arrays.toString(chArr));
        log.info("index of B  = {}", Arrays.binarySearch(chArr, 'B'));
        log.info("= After sorting =");
        Arrays.sort(chArr);
        log.info("chArr={}", Arrays.toString(chArr));
        log.info("index of B  = {}", Arrays.binarySearch(chArr, 'B'));
    }

}
