package com.example.catalogservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
// 직렬화를 위해 Serializable 상속
public class CatalogDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;

}
