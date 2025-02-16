package io.spring.springbatchlecture.batch.reader;

import io.spring.springbatchlecture.exception.CustomSkipException;
import org.springframework.aop.support.AopUtils;
import org.springframework.batch.item.ItemReader;

import java.util.LinkedList;
import java.util.List;

public class LinkedListItemReader<T> implements ItemReader<T> {

    private List<T> list;

    public LinkedListItemReader(List<T> list) {
        if (AopUtils.isAopProxy(list)) {
            this.list = list;
        } else {
            this.list = new LinkedList<>(list);
        }
    }

    @Override
    public T read() throws CustomSkipException {

        if (!list.isEmpty()) {
            T remove = list.removeFirst();

//            if ((Integer) remove == 3) {
//                throw new CustomSkipException("Read skipped : " + remove);
//            }

            System.out.println("read : " + remove);
            return remove;
        }
        return null;
    }

}
