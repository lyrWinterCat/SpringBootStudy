package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 병합 사용 update 방법
    public void save(Item item){
        if(item.getId()==null){ //id가 없다 >> 완전히 새로 생성한 객체 (신규등록-save)
            em.persist(item);
        }else{ //id가 있다 >> 이미 db에 등록이 된 객체 (update)
            //Item merge=em.merge(item); //병합 사용 update 방법
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
