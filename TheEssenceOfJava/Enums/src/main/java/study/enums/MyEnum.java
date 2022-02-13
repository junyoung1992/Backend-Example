package study.enums;

abstract public class MyEnum<T extends MyEnum<T>> implements Comparable<T> {

    static int id = 0;
    private final int ordinal;
    protected String name = "";

    public int ordinal() {
        return ordinal;
    }

    public MyEnum(String name) {
        this.name = name;
        ordinal = id++;
    }

    @Override
    public int compareTo(T o) {
        return ordinal - o.ordinal();
    }
}
