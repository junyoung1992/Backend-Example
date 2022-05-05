package me.whiteship.refactoring._04_long_parameter_list._16_combine_functinos_into_class;

import java.util.HashMap;
import java.util.Map;

public record Participant(String username, Map<Integer, Boolean> homework) {
    public Participant(String username) {
        this(username, new HashMap<>());
    }

    public void setHomeworkDone(int index) {
        this.homework.put(index, true);
    }

    public double getRate(int total) {
        long count = this.homework.values().stream()
                .filter(v -> v == true)
                .count();
        return (double) (count * 100 / total);
    }

}
