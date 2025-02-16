package io.spring.springbatchlecture.mapper;

import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.entity.Customer2;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer2 toCustomer2(Customer customer);

}
