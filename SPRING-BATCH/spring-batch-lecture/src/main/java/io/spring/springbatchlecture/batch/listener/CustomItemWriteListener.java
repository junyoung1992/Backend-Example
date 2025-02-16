package io.spring.springbatchlecture.batch.listener;

import io.spring.springbatchlecture.entity.Customer;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class CustomItemWriteListener implements ItemWriteListener<Customer> {

    @Override
    public void beforeWrite(Chunk<? extends Customer> items) {
    }

    @Override
    public void afterWrite(Chunk<? extends Customer> items) {
        System.out.println("Thread = " + Thread.currentThread().getName() + ", write item : " + items.size());
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends Customer> items) {
    }

}
