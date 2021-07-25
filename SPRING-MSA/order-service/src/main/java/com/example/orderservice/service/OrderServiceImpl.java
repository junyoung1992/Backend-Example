package com.example.orderservice.service;

import com.example.orderservice.client.CatalogServiceClient;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.example.orderservice.vo.ResponseCatalog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    CatalogServiceClient catalogServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            CatalogServiceClient catalogServiceClient,
                            CircuitBreakerFactory circuitBreakerFactory) {
        this.orderRepository = orderRepository;
        this.catalogServiceClient = catalogServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());

        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseCatalog responseCatalog = circuitbreaker.run(
                () -> catalogServiceClient.getCatalogs(orderDto.getProductId()),
                throwable -> null);

        // 예외처리 나중에 제대로 할 것
        if (responseCatalog == null)
            orderDto.setUnitPrice(0);
        else
            orderDto.setUnitPrice(responseCatalog.getUnitPrice());

        log.info(String.format("ProductId: %s - Price: %d", orderDto.getProductId(), orderDto.getUnitPrice()));

        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        // orderDto -> orderEntity
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

        orderRepository.save(orderEntity);

        return mapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

        return new ModelMapper().map(orderEntity, OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Iterable<OrderEntity> getOrderByAll() {
        return orderRepository.findAll();
    }

}
