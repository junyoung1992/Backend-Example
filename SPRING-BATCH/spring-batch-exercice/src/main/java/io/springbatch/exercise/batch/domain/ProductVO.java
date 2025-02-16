package io.springbatch.exercise.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVO {

    private Long id;
    private String name;
    private Integer price;
    private String type;

    public ProductVO() {
    }

    @Builder
    public ProductVO(Long id, String name, Integer price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

}
