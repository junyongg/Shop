package jpabook.jpabook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //짜놓은 생성 메서드 외에 다른 스타일로 다른사람이 코딩하는걸 막기 위해서 생성자 호출을 막아놓는 코드 생성자 protected 와 같은거임
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //즉시로딩은 JPQL을 실행할 때 개복잡해짐. 실무에서 모든 연관관계는 지연로딩(LAZY)로 설정해야함.
    //XtoOne(OneToOne, ManyToOne)관계는 기본이 즉시로딩이므로 지연로딩(LAZY)로설정해야 한다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //ORDINAL로 사용하면 숫자로 들어가서 나중에 status가 중간에 다른 값이 들어가면 바로 개망함. 절대 쓰지말것
    private OrderStatus status; //[ORDER] [CANCLE]

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //== 생성 메서드 ==//
    public  static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCLE);
        for (OrderItem orderItem : orderItems ){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     *  전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }



}
