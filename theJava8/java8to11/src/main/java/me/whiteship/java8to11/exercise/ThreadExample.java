package me.whiteship.java8to11.exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExample {

    public static void main(String[] args) throws InterruptedException {
        // Thread 생성 1
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("Hello: " + Thread.currentThread().getName());

        // Thread 생성 2
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread: " + Thread.currentThread().getName());
            }
        });
        thread.start();

        Thread thread1 = new Thread(() -> System.out.println("Thread: " + Thread.currentThread().getName()));
        thread1.start();

        // Interrupt
        Thread thread2 = new Thread(() -> {
           while(true) {
               System.out.println("Thread: " + Thread.currentThread().getName());
               try {
                   Thread.sleep(1000L);
               } catch (InterruptedException e) {
                   System.out.println("exit!");
                   return;
               }
           }
        });
        thread2.start();

        Thread.sleep(3000L);
        thread2.interrupt();

        // join
        Thread thread3 = new Thread(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
        thread3.start();

        thread3.join();
        System.out.println(thread3 + "is finished");

        // Executor
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> System.out.println("Thread: " + Thread.currentThread().getName()));
        // Executor는 동작시 shutdown 필요
        executorService.shutdown();
//        executorService.shutdownNow();
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread: " + Thread.currentThread().getName());
        }
    }

}
