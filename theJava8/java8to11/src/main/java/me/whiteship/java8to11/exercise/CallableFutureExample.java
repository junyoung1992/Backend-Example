package me.whiteship.java8to11.exercise;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CallableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> hello = () -> {
           Thread.sleep(2000L);
           return "Hello";
        };

        Future<String> helloFuture = executorService.submit(hello);
        System.out.println(helloFuture.isDone());
        System.out.println("Started");

//        helloFuture.get();
        helloFuture.cancel(false);

        System.out.println(helloFuture.isDone());
        System.out.println("End");
        executorService.shutdown();

        ExecutorService executorService2 = Executors.newSingleThreadExecutor();

        Callable<String> java = () -> {
            Thread.sleep(3000L);
            return "Java";
        };

        Callable<String> junyoung = () -> {
            Thread.sleep(1000L);
            return "Junyoung";
        };

        // invokeAll은 세 개가 다 끝날 때까지 기다림
        List<Future<String>> futures = executorService2.invokeAll(Arrays.asList(hello, java, junyoung));
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }
        executorService2.shutdown();

        // invokeAny는 먼저 끝나는거 출력
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        String s = executorService3.invokeAny(Arrays.asList(hello, java, junyoung));
        System.out.println(s);
        executorService3.shutdown();
    }

}
