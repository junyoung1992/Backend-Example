package me.whiteship.java8to11;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
//        Function<Integer, String> intToString = i -> "number";
        UnaryOperator<String> hi = Greeting::hi;
        System.out.println(hi.apply("Junyoung"));
        System.out.println("=============================");

        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("Junyoung"));
        System.out.println("=============================");

        Supplier<Greeting> newGreeting = Greeting::new;

        Function<String, Greeting> junyoungGreeting = Greeting::new;
        Greeting junyoung = junyoungGreeting.apply("Junyoung");
        System.out.println(junyoung.getName());
        System.out.println("=============================");

        String[] names = {"junyoung", "whiteship", "toby"};
//        Arrays.sort(names, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareToIgnoreCase(o2);
//            }
//        });
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
        System.out.println("=============================");

        FooInterface fooInterface = new DefaultFoo("junyoung");
        fooInterface.printName();
        fooInterface.printNameUpperCase();

        FooInterface.printAnything();
        System.out.println("=============================");

        List<String> name = new ArrayList<>();
        name.add("junyoung");
        name.add("whiteship");
        name.add("toby");
        name.add("foo");

        name.forEach(System.out::println);

        System.out.println("=============================");
        Spliterator<String> spliterator = name.spliterator();
        Spliterator<String> spliterator1 = spliterator.trySplit();
        while (spliterator.tryAdvance(System.out::println));

        System.out.println("=============================");
        while (spliterator1.tryAdvance(System.out::println));

        System.out.println("=============================");
        long j = name.stream()
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("J"))
                .count();
        System.out.println(j);

        System.out.println("=============================");
        name.removeIf(s -> s.startsWith("J"));
        name.forEach(System.out::println);

        System.out.println("=============================");
        name.sort(String::compareToIgnoreCase);
        name.forEach(System.out::println);
//        Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
//        name.sort(compareToIgnoreCase.reversed().thenComparing());
//        name.forEach(System.out::println);

        System.out.println("=============================");
        List<String> collect = name.stream()
//                .map( s -> {
//                    System.out.println(s);
//                    return s.toUpperCase();})
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        collect.forEach(System.out::println);

        System.out.println("=============================");

    }

}
