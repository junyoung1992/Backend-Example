package me.whiteship.java8to11.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE_PARAMETER)
@Target(ElementType.TYPE_USE)   // Type 을 사용하는 모든 곳에 사용 가능
@Repeatable(ChickenContainer.class)
public @interface Chicken {

    String value();

}
