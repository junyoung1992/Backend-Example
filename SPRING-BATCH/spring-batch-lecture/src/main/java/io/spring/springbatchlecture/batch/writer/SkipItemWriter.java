package io.spring.springbatchlecture.batch.writer;

import io.spring.springbatchlecture.exception.SkippableException;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class SkipItemWriter implements ItemWriter<String> {

    private int count = 0;

    @Override
    public void write(Chunk<? extends String> items) throws Exception {

        for (String item : items) {
            if ("-12".equals(item)) {
                count++;
                throw new SkippableException("ItemWriter failed. count = " + count);
            }

            System.out.println("ItemWriter : " + item);
        }
    }

}
