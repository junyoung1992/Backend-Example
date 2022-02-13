package study.generics;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@Slf4j
public class WildcardTest {

    @Test
    public void test() {
        FruitBox<Fruit> fruitBox = new FruitBox<>();
        FruitBox<Apple> appleBox = new FruitBox<>();

        fruitBox.add(new Apple());
        fruitBox.add(new Grape());
        appleBox.add(new Apple());
        appleBox.add(new Apple());

        log.info("{}", Juicer.makeJuice(fruitBox));
        log.info("{}", Juicer.makeJuice(appleBox));
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

    static class Box<T> {
        ArrayList<T> list = new ArrayList<>();

        void add (T item) {
            list.add(item);
        }

        T get(int i) {
            return list.get(i);
        }

        ArrayList<T> getList() {
            return list;
        }

        int size() {
            return list.size();
        }

        public String toString() {
            return list.toString();
        }
    }

    static class FruitBox<T extends Fruit> extends Box<T> {}

    static class Juice {
        String name;

        Juice(String name) {
            this.name = name + " Juice";
        }

        public String toString() {
            return name;
        }
    }

    static class Juicer {
        static Juice makeJuice(FruitBox<? extends Fruit> box) {
            String tmp = "";
            for (Fruit f: box.getList()) {
                tmp += f + " ";
            }
            return new Juice(tmp);
        }
    }

}
