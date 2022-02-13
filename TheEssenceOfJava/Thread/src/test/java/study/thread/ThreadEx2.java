package study.thread;

import org.junit.jupiter.api.Test;

public class ThreadEx2 {

    @Test
    public void test() {
        ThreadEx2_1 t1 = new ThreadEx2_1();
        t1.start();
    }

    static class ThreadEx2_1 extends Thread {
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
