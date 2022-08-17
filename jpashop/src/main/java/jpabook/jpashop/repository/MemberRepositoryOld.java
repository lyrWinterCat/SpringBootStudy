package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository //spring bean 등록
@RequiredArgsConstructor
public class MemberRepositoryOld {
    
    //@PersistenceContext
    @Autowired
    private final EntityManager em;
    //@PersistenceUnit : EntityFactory 직접사용 어노테이션

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }
    
    public List<Member> findAll(){
        
        return em.createQuery("select m from Member m", Member.class).getResultList(); //entity 객체를 대상으로 한 쿼리문
    }
    
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class).setParameter("name",name).getResultList();
    }

}
