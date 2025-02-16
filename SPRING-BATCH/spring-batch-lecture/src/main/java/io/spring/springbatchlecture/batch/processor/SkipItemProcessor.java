package io.spring.springbatchlecture.batch.processor;

import io.spring.springbatchlecture.exception.SkippableException;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class SkipItemProcessor implements ItemProcessor<String, String> {

    private int count = 0;

    @Override
    public String process(String item) throws Exception {

        if (List.of("6", "7").contains(item)) {
            count++;
            throw new SkippableException("ItemProcess failed. count = " + count);
        }

        System.out.println("ItemProcess : " + item);
        return String.valueOf(Integer.parseInt(item) * -1);
    }

}
