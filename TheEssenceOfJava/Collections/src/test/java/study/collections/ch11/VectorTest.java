package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Vector;

@Slf4j
public class VectorTest {

    @Test
    public void test01() {
        Vector v = new Vector(5);
        v.add("1");
        v.add("2");
        v.add("3");
        print(v);

        v.trimToSize(); // 빈 공간을 없앤다. (용량과 크기가 같아진다.)
        log.info("=== After trimToSize() ===");
        print(v);

        v.ensureCapacity(6);
        log.info("=== After ensureCapacity() ===");
        print(v);

        v.setSize(7);
        log.info("=== After setSize(7) ===");
        print(v);

        v.clear();
        log.info("=== After clear() ===");
        print(v);
    }

    public static void print(Vector v) {
        log.info("{}", v);
        log.info("size: {}", v.size());
        log.info("capacity: {}", v.capacity());
    }

}
