package study.thread;

import org.junit.jupiter.api.Test;

public class ThreadEx3 {

    @Test
    public void test() {
        ThreadEx3_1 t1 = new ThreadEx3_1();
        t1.run();
    }

    static class ThreadEx3_1 extends Thread {
        @Override
        public void run() {
            throwException();
        }

        public void throwException() {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
