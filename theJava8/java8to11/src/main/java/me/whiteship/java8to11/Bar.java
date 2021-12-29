package me.whiteship.java8to11;

public interface Bar extends FooInterface {

    @Override
    default void printNameUpperCase() {
        System.out.println("BAR");
    }

}
