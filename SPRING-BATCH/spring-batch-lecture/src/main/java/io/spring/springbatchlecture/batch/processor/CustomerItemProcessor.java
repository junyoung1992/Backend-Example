package io.spring.springbatchlecture.batch.processor;

import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.entity.Customer2;
import io.spring.springbatchlecture.mapper.CustomerMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer2> {

    @Override
    public Customer2 process(Customer item) {
        return CustomerMapper.INSTANCE.toCustomer2(item);
    }

}
