package hello.itemservice.domain.item;

import lombok.Data;

/**
 * @Data를 사용하면 의도하지 않는 것을 더 만들어낼 수 있으므로
 * @Getter, @Setter 등 꼭 필요한 것만 호출해서 사용하는 것이 좋음
 */
@Data
public class Item {

    private Long id;
    private String itemName;
    // 가격, 수량이 아직 정해지지 않을 수 있으므
    private Integer price;
    private Integer quantity;

    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
