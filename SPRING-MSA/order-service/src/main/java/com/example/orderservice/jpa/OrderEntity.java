package com.example.orderservice.jpa;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

    /**
     * create table orders
     * (id int auto_increment primary key,
     *  user_id varchar(50) not null,
     *  product_id varchar(20) not null,
     *  order_id varchar(50) not null,
     *  order_datetime datetime not null,
     *  qty int default 0,
     *  unit_price int default 0,
     *  total_price int default 0,
     *  created_at datetime default now());
     */

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String productId;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Date orderDatetime;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;

}
