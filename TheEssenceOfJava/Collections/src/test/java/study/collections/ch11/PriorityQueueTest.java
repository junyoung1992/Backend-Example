package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@Slf4j
public class PriorityQueueTest {

    @Test
    public void test() {
        Queue<Integer> pq = new PriorityQueue<>();
//        Queue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        pq.offer(3);
        pq.offer(1);
        pq.offer(5);
        pq.offer(2);
        pq.offer(4);

        log.info("{}", pq);

        Object obj = null;
        while ((obj = pq.poll()) != null) {
            log.info("{}", obj);
        }
    }

}
