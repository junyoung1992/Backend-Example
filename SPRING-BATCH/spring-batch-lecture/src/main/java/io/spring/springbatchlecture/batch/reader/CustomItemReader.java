package io.spring.springbatchlecture.batch.reader;

import io.spring.springbatchlecture.batch.model.Customer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class CustomItemReader implements ItemReader<Customer> {

    private final List<Customer> list;

    public CustomItemReader(List<Customer> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public Customer read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        if (list.isEmpty()) {
            return null;
        }

        return list.removeFirst();
    }

}
