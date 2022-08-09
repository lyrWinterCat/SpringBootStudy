package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계까 연결되는 것
: 의존관계주입
>> 객체 인스턴스 생성, 그 참조값을 전달해서 연결
>> 클라이언트 코드를 변경하지 않고 클라이언트가 호출하는 대상의 타입 인스턴스를 변경할 수 있음
>> 정적인 클래스 의존관계를 변경하지 않고 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있음!

Spring 설정으로 바꾸기
@Configuration : 스프링에 해당 클래스를 설정(구성)정보로 사용한다고 등록
@Bean : 메서드를 모두 호출, 반환된 객체를 스프링에 등록

>> 스프링 컨테이너를 사용하면 어떤 장점??
 */

@Configuration
public class AppConfig { //관심사 분리 (=공연 기획자) >> 배우는 연기만 신경쓰도록, 배역을 정하는 것은 공연기획자 역할!
    // 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것
    // : loC 컨테이너 / DI 컨테이너 (최근 표현)
    // == 어셈블러, 오브젝트 팩토리

    //@Bean memberService -> new MemoryMemberRepository()
    //@Bean orderService -> new MemoryMemberRepository()

    @Bean
    public MemberService memberService(){
        //return new MemberServiceImpl(new MemoryMemberRepository());
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    //refactory 1
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    //refactory2
    // 역할과 구현 클래스가 한 눈에 들어옴. (애플리케이션 전체 구성이 어떻게 되어있는지 빠르게 파악할 수 있다는 장점이 있음+중복제거)
    @Bean
    public DiscountPolicy discountPolicy(){
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public OrderService orderService(){
        //return new OrderServiceImpl(memberRepository(),new FixDiscountPolicy());
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(),discountPolicy());
        //return null; //orderServiceImpl 필드주입 test
    }

}
