package io.spring.springbatchlecture.batch.processor;

import io.spring.springbatchlecture.batch.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) {
        return new Customer(item.name().toUpperCase());
    }

}
