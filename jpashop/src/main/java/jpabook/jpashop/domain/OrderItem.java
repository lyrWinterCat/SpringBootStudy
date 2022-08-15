package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED) //new OrderItem() 와 같은 직접 생성을 막아주는 어노테이션
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //단방향 관계
    @JoinColumn(name="item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = LAZY) //order와 orderitem에서 여기가 "다" . 연관관계의 주인
    @JoinColumn(name="order_id") //하나의 order가 여러개의 orderItems를 가질 수 있음 . orderItems는 하나의 order만 가질 수 있음
    private Order order;

    private int orderPrice; //주문 가격
    private int count; // 주문 수량

    //protected OrderItem(){};
    
    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        
        item.removeStock(count); //아이템 생성한 만큼 재고 줄이기
        return orderItem;
    }


    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직 - 주문상품 전체 가격조회
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
