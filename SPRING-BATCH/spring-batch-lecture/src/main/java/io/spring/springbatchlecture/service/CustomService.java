package io.spring.springbatchlecture.service;

public class CustomService<T> {

    private int count = 0;

    public T customRead() {
        return count >= 10 ? null : (T) ("item" + count++);
    }

    public void customWrite(T item) {
        System.out.println(item);
    }

}
