package me.whiteship.java8to11.exercise;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFutureExercise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = new CompletableFuture<>();
        future.complete("Junyoung");
        System.out.println(future.get());
        System.out.println("================================================");

        // Factory Method
        CompletableFuture<String> future1 = CompletableFuture.completedFuture("Junyoung");
        System.out.println(future1.get());
        System.out.println("================================================");

        // return이 없는 작업
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        });
        future2.get();
        System.out.println("================================================");

        // return이 있는 작업
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });
        System.out.println(future3.get());
        System.out.println("================================================");

        // callback 추가 - java8 이전에는 get 이전에 callback 정의가 불가능했음
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenApply((s) -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return s.toUpperCase();
        });
        System.out.println(future4.get());

        // callback - return 없음
        CompletableFuture<Void> future5 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenAccept((s) -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            System.out.println(s.toUpperCase());
        });
        future5.get();
        System.out.println("================================================");

        // callback - 결과값 참조도 없이 특정 작업을 실행하는 경우
        CompletableFuture<Void> future6 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenRun(() -> System.out.println("Hello " + Thread.currentThread().getName()));
        future6.get();
        System.out.println("================================================");

        // 원한다면 thread pool을 만들어서 사용할 수도 있음
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> future7 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }, executorService).thenRunAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        }, executorService);
        future7.get();
        executorService.shutdown();
        System.out.println("================================================");

        // Hello가 끝난 다음에 World가 실행되어야 한다면
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });
        CompletableFuture<String> future8 = hello.thenCompose(CompletableFutureExercise::getWorld);
        System.out.println(future8.get());
        System.out.println("================================================");

        // 따로따로 실행되지만 결과는 함께 받고 싶은 경우
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });
        CompletableFuture<String> future9 = hello.thenCombine(world, (h, w) -> h + " " + w);
        System.out.println(future9.get());
        System.out.println("================================================");

        // 모두
        List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture<List<String>> result = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
        result.get().forEach(System.out::println);
        System.out.println("================================================");
        
        // 결과 하나 먼저 오는 것 출력

        CompletableFuture<Void> future10 = CompletableFuture.anyOf(hello, world)
                .thenAccept(System.out::println);
        future10.get();
        System.out.println("================================================");

        // 예외처리
        boolean throwError = false;
        CompletableFuture<String> hello2 = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).exceptionally(ex -> {
            System.out.println(ex);
            return "Error!";
        });
        hello2.get();
        System.out.println("================================================");

        // handle
        CompletableFuture<String> hello3 = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).handle((r, e) -> {
            if (e != null ) {
                System.out.println(e);
                return "Error!";
            }
            return r;
        });
        hello3.get();
        System.out.println("================================================");
    }

    private static CompletableFuture<String> getWorld(String message) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return message + " World";
        });
    }

}
