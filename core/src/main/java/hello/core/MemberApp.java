package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
        //MemberService memberService = new MemberServiceImpl(); --1

        //AppConfig appConfig = new AppConfig();
        //MemberService memberService = appConfig.memberService(); --2


        //스프링 형식으로 실행 --3
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //                                                              이름, 타입
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        //soutv 치고 엔터
        System.out.println("findMember = "+findMember.getName());
        System.out.println("member = "+member.getName());
    }
}
