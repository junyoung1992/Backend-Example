package io.spring.springbatchlecture.batch.processor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor1 implements ItemProcessor<String, String> {

    int cnt = 0;

    @Override
    public String process(String item) {
        cnt++;
        return (item + cnt).toUpperCase();
    }

}
