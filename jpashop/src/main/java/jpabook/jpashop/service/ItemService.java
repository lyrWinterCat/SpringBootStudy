package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //readOnly > 저장 안됨
@RequiredArgsConstructor
public class ItemService { //repository에 위임하는 기능만 구현하므로 간단.

        private final ItemRepository itemRepository;

        @Transactional // 메서드와 가까운 어노테이션이 우선권을 가지므로 저장 가능
        public void saveItem(Item item){
            itemRepository.save(item);
        }

        // ** 엔티티 변경시에는 항상 변경감지를 사용하는 습관을 가지자 ! **
        @Transactional
        //public Item updateItem(Long itemId, Book param){ //변경 감지 == dirty checking update 방법
        public Item updateItem(Long itemId, String name, int price, int stockQuantity){ //변경 감지 == dirty checking update 방법
            Item findItem = itemRepository.findOne(itemId);
            // findItem.change(price, name, ...) >> 이런식으로 의미있는 메서드로 set을 해주는게 좋음

            // 이렇게 set으로 쫙 까는 것은 좋은 코딩이 아님 // 실무에서 뒷목잡음
            findItem.setPrice(price);
            findItem.setName(name);
            findItem.setStockQuantity(stockQuantity);
            return findItem;

        }

        public List<Item> findItems(){
            return itemRepository.findAll();
        }

        public Item findOne(Long itemId){
            return itemRepository.findOne(itemId);
        }
}
