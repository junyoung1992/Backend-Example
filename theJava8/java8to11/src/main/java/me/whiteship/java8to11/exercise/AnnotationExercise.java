package me.whiteship.java8to11.exercise;

import me.whiteship.java8to11.annotation.Chicken;
import me.whiteship.java8to11.annotation.ChickenContainer;

import java.util.Arrays;
import java.util.List;

@Chicken("양념")
@Chicken("마늘간장")
public class AnnotationExercise {

    public static void main(String[] args) throws @Chicken("양념") RuntimeException {
        List<String> names = Arrays.asList("Junyoung");

        Chicken[] chickens = AnnotationExercise.class.getAnnotationsByType(Chicken.class);
        Arrays.stream(chickens).map(Chicken::value).forEach(System.out::println);

        ChickenContainer chickenContainer = AnnotationExercise.class.getAnnotation(ChickenContainer.class);
        Arrays.stream(chickens).map(Chicken::value).forEach(System.out::println);
    }

    static class FeelsLikeChicken<@Chicken("후라이드") T> {

        public static <@Chicken("뿌링클") C> void print(@Chicken("파닭") C c) {
            System.out.println(c);
        }

    }

}
