package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener2 implements ItemProcessListener<Integer, String> {

    @Override
    public void beforeProcess(Integer item) {
        System.out.println(">> Before process");
    }

    @Override
    public void afterProcess(Integer item, String result) {
        System.out.println(">> After process");
    }

    @Override
    public void onProcessError(Integer item, Exception e) {
        System.out.println(">> On process error");
    }

}
