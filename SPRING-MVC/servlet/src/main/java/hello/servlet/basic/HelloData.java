package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

/**
 * lombok을 사용하면 getter, setter 선언이 쉬움
 */
@Getter @Setter
public class HelloData {

    private String username;
    private int age;

}
