package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    //@Rollback(false)
    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("lee");

        //when
        Long savedId = memberService.join(member);

        //then
        //em.flush(); //insert문 확인 - db에 쿼리문 날림
        assertEquals(member,memberRepository.findOne(savedId));
    }

    //@Test(expected=IllegalStateException.class) //2. try_catch 대용으로 쓸 수 있는 테스트 어노테이션
    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        //memberService.join(member2); //예외가 발생해야해 !

        //3. assert로 던질 수도 있음
        assertThrows(IllegalStateException.class,()-> memberService.join(member2));

        //1. try_catch문 너무 지저분해..
//        memberService.join(member1);
//        try {
//            memberService.join(member2); //예외가 발생해야해 !
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        //fail("예외가 발생해야 하는데.."); //여기로 들어오면 안됨.
    }

}