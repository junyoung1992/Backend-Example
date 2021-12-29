package me.whiteship.java8to11;

public class DefaultFoo implements FooInterface, Bar {

    String name;

    public DefaultFoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    // 재정의해도 됨
    @Override
    public void printNameUpperCase() {
        System.out.println(this.name.toUpperCase());
    }

}
