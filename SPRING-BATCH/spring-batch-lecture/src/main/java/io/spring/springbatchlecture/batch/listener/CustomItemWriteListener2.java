package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class CustomItemWriteListener2 implements ItemWriteListener<String> {

    @Override
    public void beforeWrite(Chunk<? extends String> items) {
        System.out.println(">> Before write");
    }

    @Override
    public void afterWrite(Chunk<? extends String> items) {
        System.out.println(">> After write");
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends String> items) {
        System.out.println(">> On write error");
    }

}
