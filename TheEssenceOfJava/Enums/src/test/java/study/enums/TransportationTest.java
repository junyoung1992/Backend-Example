package study.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransportationTest {

    @Test
    public void test01() {
        System.out.println("bus fare=" + Transportation.BUS.fare(100));
        System.out.println("train fare=" + Transportation.TRAIN.fare(100));
        System.out.println("ship fare=" + Transportation.SHIP.fare(100));
        System.out.println("airplane fare=" + Transportation.AIRPLANE.fare(100));
    }

}