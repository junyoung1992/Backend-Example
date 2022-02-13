package study.thread;

import org.junit.jupiter.api.Test;

public class ThreadEx4 {

    static long globalStartTime = 0;

    @Test
    public void test1() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 300; i++) {
            System.out.printf("%s", new String("-"));
        }
        System.out.println("소요시간1: " + (System.currentTimeMillis() - startTime));

        for (int i = 0; i < 300; i++) {
            System.out.printf("%s", new String("|"));
        }
        System.out.println("소요시간2: " + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void test2() {
        ThreadEx5_1 th1 = new ThreadEx5_1();
        th1.start();
        globalStartTime = System.currentTimeMillis();

        for (int i = 0; i < 300; i++) {
            System.out.printf("%s", new String("-"));
        }
        System.out.println("소요시간1: " + (System.currentTimeMillis() - globalStartTime));
    }

    static class ThreadEx5_1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 300; i++) {
                System.out.printf("%s", new String("|"));
            }
            System.out.println("소요시간2: " + (System.currentTimeMillis() - globalStartTime));
        }
    }

}
