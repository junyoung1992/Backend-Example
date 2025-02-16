package io.spring.springbatchlecture.batch.processor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor5 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

    @Override
    public ProcessorInfo process(ProcessorInfo item) {
        System.out.println("ItemProcessor5");
        return item;
    }

}
