package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener2 implements ItemReadListener<Integer> {

    @Override
    public void beforeRead() {
        System.out.println(">> Before read");
    }

    @Override
    public void afterRead(Integer item) {
        System.out.println(">> After read");
    }

    @Override
    public void onReadError(Exception ex) {
        System.out.println(">> On read error");
    }

}
