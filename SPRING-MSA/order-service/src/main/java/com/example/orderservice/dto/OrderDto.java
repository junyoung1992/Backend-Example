package com.example.orderservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
// 직렬화를 위해 Serializable 상속
public class OrderDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderDatetime;

    private String orderId;
    private String userId;

}
