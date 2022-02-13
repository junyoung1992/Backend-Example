package study.generics;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@Slf4j
public class FruitBoxTest2 {

    @Test
    public void test() {
        FruitBox<Fruit> fruitBox = new FruitBox<>();
        FruitBox<Apple> appleBox = new FruitBox<>();
        FruitBox<Grape> grapeBox = new FruitBox<>();
//        FruitBox<Grape> grapeBox = new FruitBox<Apple>(); 에러. 타입 불일치
//        FruitBox<Toy> toyBox = new FruitBox<>();  에러

        fruitBox.add(new Fruit());
        fruitBox.add(new Apple());
        fruitBox.add(new Grape());
        appleBox.add(new Apple());
//        appleBox.add(new Grape());    에러. Grape는 Apple의 자손이 아님
        grapeBox.add(new Grape());

        log.info("fruitBox-{}", fruitBox);
        log.info("appleBox-{}", appleBox);
        log.info("grapeBox-{}", grapeBox);
    }

    interface Eatable {}

    static class Fruit implements Eatable {
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

    static class FruitBox<T extends Fruit & Eatable> extends Box<T> {}

}
