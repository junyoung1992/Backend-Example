package me.whiteship.refactoring._11_primitive_obsession._30_repliace_primitive_with_object;

import java.util.List;

public class Priority {

    private String value;
    private List<String> legalValue = List.of("low", "normal", "high", "rush");

    public Priority(String value) {
        if (legalValue.contains(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Illegal value for priority " + value);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    private int index() {
        return legalValue.indexOf(this.value);
    }

    public boolean higherThan(Priority other) {
        return this.index() > other.index();
    }

}
