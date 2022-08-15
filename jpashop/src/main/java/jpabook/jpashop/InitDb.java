package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    
    private final InitService initService;
    
    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }
    

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        
        private final EntityManager em;
        
        public void dbInit1(){
            Member member = createMember("userA", "인천", "1", "1111");
            em.persist(member);

            Book book1 = new Book();
            createBook(book1, "JPA1 BOOK", 10000, 100);
            em.persist(book1);
            
            Book book2 = new Book();
            createBook(book2, "JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2); //여기서 order가 배열로 넘어감
            em.persist(order);
        }

        public void dbInit2(){
            Member member = createMember("userB", "서울", "2", "2222");
            em.persist(member);

            Book book1 = new Book();
            createBook(book1, "Spring1 BOOK", 30000, 300);
            em.persist(book1);

            Book book2 = new Book();
            createBook(book2, "Spring2 BOOK", 40000, 400);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 30000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2); //여기서 order가 배열로 넘어감
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private void createBook(Book book, String name, int price, int stockQuantity) {
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}
