package study.generics;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Slf4j
public class WildcardTest2 {

    @Test
    public void test() {
        FruitBox<Apple> appleBox = new FruitBox<>();
        FruitBox<Grape> grapeBox = new FruitBox<>();

        appleBox.add(new Apple("GreenApple", 300));
        appleBox.add(new Apple("GreenApple", 100));
        appleBox.add(new Apple("GreenApple", 200));

        grapeBox.add(new Grape("GreenGrape", 400));
        grapeBox.add(new Grape("GreenGrape", 300));
        grapeBox.add(new Grape("GreenGrape", 200));

        Collections.sort(appleBox.getList(), new AppleComp());
        Collections.sort(grapeBox.getList(), new GrapeComp());

        log.info("{}", appleBox);
        log.info("{}", grapeBox);

        Collections.sort(appleBox.getList(), new FruitComp());
        Collections.sort(grapeBox.getList(), new FruitComp());

        log.info("{}", appleBox);
        log.info("{}", grapeBox);
    }

    static class Fruit {
        String name;
        int weight;

        public Fruit(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public String toString() {
            return name + "(" + weight + ")";
        }
    }

    static class Apple extends Fruit {
        public Apple(String name, int weight) {
            super(name, weight);
        }
    }

    static class Grape extends Fruit {
        public Grape(String name, int weight) {
            super(name, weight);
        }
    }

    static class FruitComp implements Comparator<Fruit> {
        public int compare(Fruit t1, Fruit t2) {
            return t1.weight - t2.weight;
        }
    }

    static class AppleComp implements Comparator<Apple> {
        public int compare(Apple t1, Apple t2) {
            return t2.weight - t1.weight;
        }
    }

    static class GrapeComp implements Comparator<Grape> {
        public int compare(Grape t1, Grape t2) {
            return t2.weight - t1.weight;
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
    
}
