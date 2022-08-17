package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제 >> @JsonIgnore 어노테이션 필수
 *
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
 * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경
 가능)
 *
 * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
 * - 페이징 가능
 * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
 * - 페이징 가능
 * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
 * - 페이징 불가능...
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;



    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            //프록시 강제 초기화
            order.getMember().getName();
            order.getDelivery().getAddress();
            
            List<OrderItem> orderItems = order.getOrderItems(); //orderItems 강제 초기화

            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName(); // orderItem안의 item들도 초기화
            }
            //orderItems.stream().forEach(o->o.getItem().getName());
            
            // 양방향 관계는 @JsonIgnore로 꼭 막아주어야 에러가 나지 않음 ! 
        }
        return all;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem(); //데이터 4개 소유 -> 일대 다 에서 다의 개수만큼 데이터 소유

//        for (Order order : orders) {
//            System.out.println("order ref = " + order+ " id="+order.getId());
//        }

        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        return collect;
    }

    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v3.2/orders")
    public List<jpabook.jpashop.service.query.OrderDto> ordersV3_query(){
        return orderQueryService.ordersV3();
    }



    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value="offset",defaultValue="0") int offset,
                                        @RequestParam(value="limit",defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit); // member, delivery를 fetch join 해 놓은 쿼리문
        // orders에 4, 11번 user가 있음 >> JPA에서 user의 orderItems를 in쿼리로 미리 가져옴 >> 단 하나의 쿼리로!
        // offset, limit 사이즈를 잘 짜는 것이 중요!

        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6(){
        //return orderQueryRepository.findAllByDto_flat(); //데이터 중복 발생

        // 스펙을 맞추고 싶어! (나도 OrderQueryDto로 반환하고싶어! >>노가다)
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }



    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        //루프 돌면서 orderDTO로 변환 (생성자 넘김 - orders에서 루프 돌림)
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        return collect;
    }

    //@Data
    @Getter
    static class OrderDto{

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

    @Getter
    static class OrderItemDto{
        //내가 외부로 보내고 싶은 값만 가져오기
        private String itemName; //상품명
        private int orderPrice; //주문 가격
        private int count; // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }





}
