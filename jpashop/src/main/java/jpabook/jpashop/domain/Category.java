package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    // 카테고리-아이템 다대다 매핑 (예제일 뿐, 실무에선 쓰지 않음-필드추가 불가능)
    // joinTable 필요 ! (중간테이블 매핑 필요, 일대다-다대일 관계로 풀어내기 용도)
    @ManyToMany
    @JoinTable(name="category_item",
            joinColumns = @JoinColumn(name="category_id"), //들어오는 테이블
                inverseJoinColumns = @JoinColumn(name="item_id")) //나가는 테이블
    private List<Item> items = new ArrayList<>();



    //계층구조 매핑 1
    @ManyToOne(fetch = LAZY) // *toOne 코드는 기본 fetch가 eager 이므로 lazy로 다 바꿔주어야 한다.
    @JoinColumn(name="parent_id") //주인, 부모
    private Category parent;

    //계층구조 매핑 2
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); //거울, 자식

    //==연관관계 편의 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
