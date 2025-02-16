package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSkipListener implements SkipListener<Integer, String> {

    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println(">> On skip in read");
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        System.out.println(">> On skip in write");
    }

    @Override
    public void onSkipInProcess(Integer item, Throwable t) {
        System.out.println(">> On skip in process");
    }

}
