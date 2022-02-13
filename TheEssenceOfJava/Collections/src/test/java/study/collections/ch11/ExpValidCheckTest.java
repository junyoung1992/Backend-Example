package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.Stack;

@Slf4j
public class ExpValidCheckTest {

    @Test
    public void test() {
        Stack st = new Stack();
        String expression = "((2+3)*1)+3";

        log.info("expression={}", expression);

        try {
            for (int i = 0; i < expression.length(); i++) {
                char ch = expression.charAt(i);

                if (ch == '(') {
                    st.push(ch);
                } else if (ch == ')') {
                    st.pop();
                }
            }

            if (st.isEmpty()) {
                log.info("괄호가 일치합니다.");
            } else {
                log.info("괄호가 일치하지 않습니다.");
            }
        } catch (EmptyStackException e) {
            log.info("괄호가 일치하지 않습니다.");
        }
    }

}
