package jpabook.jpabook.domain;

import jakarta.persistence.*;
import jpabook.jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문 가격
    private int count;//주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }


    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
    //필드에 직접 접근하지 않고 getOrderPrice는 사실 별의미 없음 객체 내에선 필드로 접근해도 되고 getOrderPrice로 접근해도됨.
    //근데 JPA 프록시를 다루면 중요해진다고함. 이건 JPA고급에서 듣는걸루 ...ㅎㅎ
}
