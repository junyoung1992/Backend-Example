package io.spring.springbatchlecture.batch.processor;

import io.spring.springbatchlecture.exception.CustomRetryException;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor6 implements ItemProcessor<Integer, String> {

    int count = 0;

    @Override
    public String process(Integer item) throws Exception {

        if (count < 2) {
            if (count % 2 == 0) {
                count = count + 1;

            } else if (count % 2 == 1) {
                count = count + 1;
                throw new CustomRetryException();
            }
        }
        System.out.println("process : " + item);
        return String.valueOf(item);
    }

}
