package jpabook.jpabook.domain;


import jakarta.persistence.*;
import jpabook.jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // 실무에서는 매니투매니 사용 x
    // 중간테이블에 컬럼을 추가할 수가 없고, 세밀하고 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있음.
    // 따라서 중간엔티티 CategoryItem를 만들고 @ManyToOne, @OneToMany로 매핑해서 사용하자
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private  Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    //==연관관계 메서드==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
