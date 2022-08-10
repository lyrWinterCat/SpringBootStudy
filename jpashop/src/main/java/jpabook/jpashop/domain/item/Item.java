package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 전략 - 테이블 하나에 다 넣을거야 : single_table
@DiscriminatorColumn(name="dtype") // 구분 타입 설정. 상속 아이템의 @DiscriminatorValue("설정값") 로 설정
@Getter @Setter
public abstract class Item  {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    //공통속성
    private String name;
    private int price;
    private int stockQuantity;

    //상속할 아이템 만들기 class Album, Book, Movie
    //상속관계 매핑 전략을 잘 세워야 함

    //카테고리-아이템의 다대다 매핑
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직

    //stock 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    //stock감소
    public void removeStock(int quantity){
        int restStock = stockQuantity-quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }

}
