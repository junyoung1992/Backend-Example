package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@Slf4j
public class StackQueueTest {

    @Test
    public void test01() {
        Stack st = new Stack();
        Queue q = new LinkedList(); // Queue 인터페이스의 구현체인 LinkedList를 사용

        st.push("0");
        st.push("1");
        st.push("2");

        q.offer("0");
        q.offer("1");
        q.offer("2");

        log.info("= Stack =");
        while(!st.empty()) {
            log.info("{}", st.pop());
        }

        log.info("= Queue =");
        while(!q.isEmpty()) {
            log.info("{}", q.poll());
        }
    }

}
