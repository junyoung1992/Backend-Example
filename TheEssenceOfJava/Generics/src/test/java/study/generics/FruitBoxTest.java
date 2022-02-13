package study.generics;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@Slf4j
public class FruitBoxTest {

    @Test
    public void test() {
        Box<Fruit> fruitBox = new Box<>();
        Box<Apple> appleBox = new Box<>();
        Box<Toy> toyBox = new Box<>();
//        Box<Grape> grapeBox = new Box<Apple>();   // 에러. 타입 불일치

        fruitBox.add(new Fruit());
        fruitBox.add(new Apple());

        appleBox.add(new Apple());
        appleBox.add(new Apple());
//        appleBox.add(new Toy());

        toyBox.add(new Toy());
//        toyBox.add(new Apple());

        log.info("{}", fruitBox);
        log.info("{}", appleBox);
        log.info("{}", toyBox);
    }

    static class Fruit {
        public String toString() {
            return "Fruit";
        }
    }

    static class Apple extends Fruit {
        public String toString() {
            return "Apple";
        }
    }

    static class Grape extends Fruit {
        public String toString() {
            return "Grape";
        }
    }

    static class Toy {
        public String toString() {
            return "Fruit";
        }
    }

    static class Box<T> {
        ArrayList<T> list = new ArrayList<>();

        void add (T item) {
            list.add(item);
        }

        T get(int i) {
            return list.get(i);
        }

        int size() {
            return list.size();
        }

        public String toString() {
            return list.toString();
        }
    }

}
