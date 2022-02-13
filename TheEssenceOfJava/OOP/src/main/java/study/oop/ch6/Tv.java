package study.oop.ch6;

public class Tv {

    String color;   // 색상
    boolean power;  // 전원 상태 (on/off)
    int channel;    //

    /**
     * TV를 켜거나 끄는 기능을 하는 메서드
     */
    void power() {
        power = !power;
    }

    /**
     * TV의 채널을 높이는 기능을 하는 메서드
     */
    void channelUp() {
        ++channel;
    }

    /**
     * TV의 채널을 낮추는 기능을 하는 메서드
     */
    void channelDown() {
        --channel;
    }

}
