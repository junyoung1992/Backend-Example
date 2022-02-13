package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

@Slf4j
public class IteratorTest {

    @Test
    public void test01() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String obj = it.next();
            log.info("{}", obj);
        }
    }

    @Test
    public void test02() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        ListIterator<String> it = list.listIterator();

        while (it.hasNext()) {
            log.info("{}", it.next());
        }

        while (it.hasPrevious()) {
            log.info("{}", it.previous());
        }
    }

    @Test
    public void test03() {
        ArrayList original = new ArrayList(10);
        ArrayList copy1 = new ArrayList(10);
        ArrayList copy2 = new ArrayList(10);

        for (int i = 0; i < 10; i++) {
            original.add(i);
        }

        Iterator it = original.iterator();
        while (it.hasNext()) {
            copy1.add(it.next());
        }

        log.info("= Original에서 copy1로 복사(copy) =");
        log.info("original:{}", original);
        log.info("copy1:{}", copy1);

        it = original.iterator();   // iterator는 재사용이 안되므로, 다시 얻어와야 한다.
        while (it.hasNext()) {
            copy2.add(it.next());
            it.remove();
        }

        log.info("= Original에서 copy2로 이동(move) =");
        log.info("original:{}", original);
        log.info("copy2:{}", copy2);
    }

}
