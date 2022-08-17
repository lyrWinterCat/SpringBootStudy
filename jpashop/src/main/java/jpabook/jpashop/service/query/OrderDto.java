package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class OrderDto{

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    //OrderDto 안에도 OrderItem 엔티티 그대로 반환하지 마! >> 또다시 orderItem DTO 생성해서 넣어줘야함
    private List<OrderItemDto> orderItems;
    // 모든 의존성을 없애야 한다.

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        //프록시 강제 초기화
        order.getOrderItems().stream().forEach(o->o.getItem().getName());
        //orderItems = order.getOrderItems();

        // OrderItem 엔티티 대신 OrderItemDto 반환
        orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(toList());
    }
}
