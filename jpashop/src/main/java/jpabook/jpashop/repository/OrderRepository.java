package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.QMember.*;
import static jpabook.jpashop.domain.QOrder.*;


@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // String + 로 조회 : jpql을 조건에 맞는 쿼리문으로 만들어준다.>> 비추천 (에러가 많음)
    public List<Order> findAllByString(OrderSearch orderSearch) {

//        return = em.createQuery("select o from Order o join o.member m"
//                + " where o.status=:status and m.name like :name", Order.class)
//                .setParameter("status",orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)//최대 1000건까지 보여줘
//                .getResultList();

        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    // JPA Criteria : jpql을 자바 코드로 짤 수 있도록 도와줌 >> 비추천 / 실무용은 아님 (유지보수성 제로 )
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class); //응답타입 세팅
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
        return query.getResultList();
    }
    // 위와 같은 단점들로 인해 나온 해결책 : QueryDSL

    //QueryDSL
    public List<Order> findAll(OrderSearch orderSearch){
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(em);
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .limit(1000)
//                .fetch();
//    }

        //compile 시점에 오타 에러가 다 잡힘
        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), //condition 넣기
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String memberName) {

        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return member.name.like(memberName);
    }

//    private BooleanExpression nameLike(OrderSearch orderSearch, QMember member){
//        if(!StringUtils.hasText(orderSearch.getMemberName())){
//            return null;
//        }
//        return QMember.member.name.like(orderSearch.getMemberName());
//    }

    //queryDsl


    //manyToOne, oneToOne 코드
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m"+
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    // oneToMany
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" //2개
                + " join fetch o.member m"
                + " join fetch o.delivery d"
                + " join fetch o.orderItems oi" //4개
                + " join fetch oi.item i", Order.class
        ).getResultList();
        // 페이징 처리 불가능 !!
//        return em.createQuery(
//                "select distinct o from Order o" //2개
//                + " join fetch o.member m"
//                + " join fetch o.delivery d"
//                + " join fetch o.orderItems oi" //4개
//                + " join fetch oi.item i", Order.class
//        ).setFirstResult(1).setMaxResults(100) //>>>불가능!!
//        .getResultList();
    }

    // v3.1 OneToMany 페이징 처리 쿼리
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m"+
                        " join fetch o.delivery d", Order.class
        ).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    // order.simplequery 패키지로 이동함~~
//    public List<OrderSimpleQueryDto> findOrderDtos(){
//        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
//                        "from Order o" +
//                " join o.member m" +
//                " join o.delivery d", OrderSimpleQueryDto.class)
//                .getResultList();
//    }


}
