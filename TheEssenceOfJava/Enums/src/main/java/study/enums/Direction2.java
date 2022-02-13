package study.enums;

public enum Direction2 {
    EAST(1, ">"), SOUTH(2, "V"), WEST(3, "<"), NORTH(4, "^");

    private static final Direction2[] DIR_ARR = Direction2.values();
    private final int value;
    private final String symbol;

    Direction2(int value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Direction2 of(int dir) {
        if (dir < 1 || dir > 4) {
            throw new IllegalArgumentException("Invalid value :" + dir);
        }
        return DIR_ARR[dir - 1];
    }

    // 방향을 회전시키는 메서드. num의 값만큼 90도씩 시계방향으로 회전한다.
    public Direction2 rotate(int num) {
        num %= 4;

        // num 이 음수일 때는 시계 반대 방향으로 회전
        if (num < 0) {
            num += 4;
        }

        return DIR_ARR[(value - 1 + num) % 4];
    }

    @Override
    public String toString() {
        return name() + getSymbol();
    }

}
