package me.whiteship.java8to11.exercise;

import me.whiteship.java8to11.sample.OnlineClass;
import me.whiteship.java8to11.sample.Progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionalExercise {

    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        OnlineClass spring_boot = new OnlineClass(1, "spring boot", true);
//        Progress progress = spring_boot.getProgress();
//        if (progress != null) {
//            System.out.println(progress.getStudyDuration());
//        }

        Optional<OnlineClass> optional = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("jpa"))
                .findFirst();

        boolean present = optional.isPresent();
        System.out.println(present);

        if (optional.isPresent()) {
            OnlineClass onlineClass = optional.get();
            System.out.println(onlineClass.getTitle());
        }

        optional.ifPresent(oc -> System.out.println(oc.getTitle()));

        // 이미 만들어져있는 상수 같은 걸 null 대신 리턴할 때 사용하면 좋음
        OnlineClass onlineClass = optional.orElse(createNewClasses());
        System.out.println(onlineClass.getTitle());

        // 동적으로 작업을 수행하여 null 대신 리턴할 때
        OnlineClass onlineClass1 = optional.orElseGet(OptionalExercise::createNewClasses);
        System.out.println(onlineClass1.getTitle());

        // 기본 -> NoSuchElementException
        OnlineClass onlineClass2 = optional.orElseThrow(IllegalStateException::new);
        System.out.println(onlineClass2.getTitle());

        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println(integer.isPresent());

        Optional<Optional<Progress>> progress = optional.map(OnlineClass::getProgress);
        Optional<Progress> progress1 = progress.orElseThrow();

        // flatMap 을 사용하면 Optional<Optional>을 한 번에 풀 수 있음
        // stream 의 flatMap과는 다름!!
        Optional<Progress> progress2 = optional.flatMap(OnlineClass::getProgress);
    }

    private static OnlineClass createNewClasses() {
        System.out.println("creating new online class");
        return new OnlineClass(10, "New class", false);
    }

}
