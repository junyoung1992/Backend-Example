package me.whiteship.java8to11;

import java.util.function.*;

public class Foo {

    public static void main(String[] args) {
        // 익명 내부 클래스 anonymous inner class
//        RunSomething runSomething = new RunSomething() {
//            @Override
//            public void doIt() {
//                System.out.println("Hello");
//                System.out.println("Lambda");
//            }
//        };

//        RunSomething runSomething = () -> {
//            System.out.println("Hello");
//            System.out.println("Lambda");
//        };
//        runSomething.doIt();

//        RunSomething runSomething = number -> number + 10;
//        System.out.println(runSomething.doIt(1));

//        Plus10 plus10 = new Plus10();
//        System.out.println(plus10.apply(1));

        Function<Integer, Integer> plus10 = i -> i + 10;
        System.out.println(plus10.apply(1));

        Function<Integer, Integer> multiply2 = i -> i * 2;
        System.out.println(multiply2.apply(2));

        // (x * 2) + 10
        Function<Integer, Integer> multiply2AndPlus10 = plus10.compose(multiply2);
        System.out.println(multiply2AndPlus10.apply(2));

        // (x + 10) * 2
        System.out.println(plus10.andThen(multiply2).apply(2));

        Consumer<Integer> printT = i -> System.out.println(i);
        printT.accept(10);

        Supplier<Integer> get10 = () -> 10;
        System.out.println(get10.get());

        // return: Boolean
        Predicate<String> startWithJunyoung = s -> s.startsWith("junyoung");
        Predicate<Integer> isEven = i -> i % 2 == 0;

        // Input Type과 Output Type이 같은 경우
        UnaryOperator<Integer> _plus10 = i -> i + 10;
        System.out.println(_plus10.apply(1));

        BinaryOperator<Integer> sum = (a, b) -> a + b;
        System.out.println(sum.apply(2, 3));

        Foo foo = new Foo();
        foo.run();
    }

    private void run() {
        // 사실상 final 일 때는 final 생략 가능
        final int baseNumber = 10;

        // 로컬 클래스
        class LocalClass {
            void printBaseNumber() {
                System.out.println(baseNumber);
            }
        }

        // 익명 클래스
        Consumer<Integer> integerConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(baseNumber);
            }
        };

        // 람다
        IntConsumer printInt = i -> System.out.println(i + baseNumber);

        printInt.accept(10);
    }

}
