package io.spring.springbatchlecture.batch.writer;

import io.spring.springbatchlecture.exception.CustomRetryException;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter2 implements ItemWriter<String> {

    private int count = 0;

    @Override
    public void write(Chunk<? extends String> items) throws CustomRetryException {
        for (String item : items) {
            if (count < 2) {
                if (count % 2 == 0) {
                    count = count + 1;

                } else if (count % 2 == 1) {
                    count = count + 1;
                    throw new CustomRetryException();
                }
            }
            System.out.println("write : " + item);
        }
    }

}
