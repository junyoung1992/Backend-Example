package io.springbatch.exercise.batch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Product {

    @Id
    private String id;
    private String name;
    private Integer price;
    private String type;

    protected Product() {
    }

    @Builder
    public Product(String id, String name, Integer price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

}
