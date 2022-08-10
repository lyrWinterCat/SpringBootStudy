package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //단방향 관계
    @JoinColumn(name="item_id")
    private Item item;

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
        
        item.removeStock(count);
        return orderItem;
    }


    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직 - 조회상품 전체 가격조회
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
