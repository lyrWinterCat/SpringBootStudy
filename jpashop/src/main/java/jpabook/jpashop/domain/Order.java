package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    //member_id FK를 가지고 있음 >> 연관관계의 주인!
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //cascade 타입을 all로 준다면 persist를 각각에 호출해주지 않아도 order 하나만 persist해 줄 수 있음
    private List<OrderItem> orderItems = new ArrayList<>(); // 여기가 연관관계 거울. mapped by(OrderItem의 order)를 통해 결정될거야 !

    // 일대일 매핑 - 근심과 걱정.. (order-delivery)
    // 어디에 FK를 두느냐에 따라 장단점이 있음 (어디에 두던 상관은 없기 때문)
    // Access를 많이 하는 쪽에 FK를 두는 쪽이 편함 (강사스타일) >> Order에 접근을 많이 하니, 여기에 두겠다.
    // 위의 결정에 따라 연관관계 주인이 정해짐 : order가 주인! > JoinColumn 설정
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) //cascade : order persist할 때 delivery도 같이 persist 할게 !
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER,CANCEL]

    // 연관관계 메서드 - 양방향일때 양쪽 세팅을 위한 메서드
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    // 주문취소

    public void cancel(){
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //조회 로직
    //전체주문가격 조회
    public int getTotalPrice(){

//        int totalPrice=0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;

        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();

    }



}
