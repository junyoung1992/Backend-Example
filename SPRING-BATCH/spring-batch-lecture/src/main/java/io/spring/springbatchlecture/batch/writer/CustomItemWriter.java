package io.spring.springbatchlecture.batch.writer;

import io.spring.springbatchlecture.batch.model.Customer;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(Chunk<? extends Customer> chunk) {
        chunk.forEach(System.out::println);
    }

}
