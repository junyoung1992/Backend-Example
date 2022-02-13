package study.thread;

import org.junit.jupiter.api.Test;

public class ThreadEx1 {

    /**
     * Thread 클래스를 상속받는 방법과 Runnable 인터페이스를 구현하는 방법 두 가지가 있다.<br>
     * Thread 클래스를 상속받으면 다른 클래스를 상속받을 수 없기 때문에 Runnable 인터페이스를 구현하는 방법이 일반적이다.<br>
     * Runnable 인터페이스를 구현하는 방법은 재사용성이 높고 코드의 일관성을 유지할 수 있기 때문에 보다 객체지향적인 방법이라 할 수 있다.
     */
    @Test
    public void test() {
        ThreadEx1_1 t1 = new ThreadEx1_1();

        Runnable r = new ThreadEx1_2();
        Thread t2 = new Thread(r);

        t1.start();
        t2.start();
    }

    static class ThreadEx1_1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(getName());  // 조상인 Thread의 getName()을 호출
            }
        }
    }

    static class ThreadEx1_2 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

}
