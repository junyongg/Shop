package jpabook.jpabook.domain;

import jakarta.persistence.*;
import jpabook.jpabook.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //짜놓은 생성 메서드 외에 다른 스타일로 다른사람이 코딩하는걸 막기 위해서 생성자 호출을 막아놓는 코드 생성자 protected 와 같은거임
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
