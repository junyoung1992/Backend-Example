package study.collections.ch11;

import java.util.*;

/**
 * Vector 클래스의 실제 코드를 바탕으로 이해하기 쉽게 재구성
 */
public class MyVector2 extends MyVector implements Iterator {

    int cursor = 0;
    int lastRet = -1;

    public MyVector2(int capacity) {
        super(capacity);
    }

    public MyVector2() {
        this(10);
    }

    @Override
    public String toString() {
        String tmp = "";
        Iterator it = iterator();

        for (int i = 0; it.hasNext(); i++) {
            if (i != 0) {
                tmp += ", ";
            }
            tmp += it.next();
        }
        return "[" + tmp + "]";
    }

    /**
     * cursor와 lastRet을 초기화한다
     * @return 초기화 된 Iterator
     */
    @Override
    public Iterator iterator() {
        cursor = 0;
        lastRet = -1;
        return this;
    }

    @Override
    public boolean hasNext() {
        return cursor != size();
    }

    @Override
    public Object next() {
        Object next = get(cursor);
        lastRet = cursor++;
        return next;
    }

    @Override
    public void remove() {
        if (lastRet == -1) {
            throw new IllegalStateException();
        } else {
            remove(lastRet);
            cursor--;
            lastRet = -1;
        }
    }

}
