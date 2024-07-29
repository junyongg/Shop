package jpabook.jpabook.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "deliver_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;


    @Enumerated(EnumType.STRING) //ORDINAL로 사용하면 숫자로 들어가서 나중에 status가 중간에 다른 값이 들어가면 바로 개망함. 절대 쓰지말것
    private DeliveryStatus status; //READY, COMP
}
