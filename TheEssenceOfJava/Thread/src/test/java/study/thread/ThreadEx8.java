package study.thread;

import org.junit.jupiter.api.Test;

public class ThreadEx8 {

    @Test
    public void test() {
        ThreadEx8_1 th1 = new ThreadEx8_1();
        ThreadEx8_2 th2 = new ThreadEx8_2();

        th2.setPriority(7);

        System.out.println("Priority of th1 (-) : " + th1.getPriority());
        System.out.println("Priority of th2 (|) : " + th2.getPriority());

        th1.start();
        th2.start();
    }

    static class ThreadEx8_1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 300; i++) {
                System.out.print("-");
                for (int x = 0; x < 10000000; x++);
            }
        }
    }

    static class ThreadEx8_2 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 300; i++) {
                System.out.print("|");
                for (int x = 0; x < 10000000; x++);
            }
        }
    }

}
