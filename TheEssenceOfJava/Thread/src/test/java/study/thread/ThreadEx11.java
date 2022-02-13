package study.thread;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

public class ThreadEx11 {

    @Test
    public void test() {
        ThreadEx11_1 t1 = new ThreadEx11_1("Thread1");
        ThreadEx11_2 t2 = new ThreadEx11_2("Thread2");
        t1.start();
        t2.start();
    }

    static class ThreadEx11_1 extends Thread {
        ThreadEx11_1(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                sleep(5 * 1000);
            } catch (InterruptedException e) {}
        }
    }

    static class ThreadEx11_2 extends Thread {
        ThreadEx11_2(String name) {
            super(name);
        }

        @Override
        public void run() {
            Map<Thread, StackTraceElement[]> map = getAllStackTraces();
            Iterator<Thread> it = map.keySet().iterator();

            int x = 0;
            while(it.hasNext()) {
                Thread t = it.next();
                StackTraceElement[] ste = map.get(t);

                System.out.println("[" + ++x + "] name : " + t.getName()
                                + ", group : " + t.getThreadGroup().getName()
                                + ", daemon : " + t.isDaemon());

                for (StackTraceElement stackTraceElement : ste) {
                    System.out.println(stackTraceElement);
                }
                System.out.println();
            }
        }
    }

}
