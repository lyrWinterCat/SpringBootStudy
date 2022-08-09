package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired ItemService itemService;
    @Autowired EntityManager em;

    @Test
    public void 아이템_등록_테스트() throws Exception{
        //given
        Item item1 = new Book();
        item1.setName("spring");

        itemService.saveItem(item1);
        //when
        em.flush();

        //then
        assertEquals(item1,itemService.findOne(item1.getId()));
    }
}