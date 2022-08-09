package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@RequiredArgsConstructor
@Component
public class OrderServiceImpl implements OrderService{

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

//1.    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//2.    private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); // fix-rate로 할인 변경
    // 추상화인 인터페이스에만 의존하도록 변경 (3) >> 생성자 주입법 >> DIP 성립
    // 생성자 주입은 언제일어나느냐?? 해당 생성자가 만들어질때! new OrderServiceImpl(memberRepository, discountPolicy)
    
    
    // private MemberRepository memberRepository;
    // private DiscountPolicy discountPolicy;
    // 한 후 setter 만들고 해당 setter에 autowired를 주어도 주입이 됨 >> 수정자 주입 / setter 주입

/*    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }*/
    // 필드주입 : 위의 private final MemberRepository, DiscountPolicy 에 @Autowired로 주입
    // * 생성자 먼저 주입, setter 주입이 다음으로 이루어짐

    //@RequiredArgsConstructor >> final이 붙은 필드를 모아서 생성자 자동생성
    
    @Autowired //생성자가 단 하나일 때에는 Autowired 생략해도 자동주입됨  + 여기는 생성자주입
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) { //생성자 주입을 위한 생성자
        //*System.out.println("memberRepository = " + memberRepository);
        //System.out.println("discountPolicy = " + discountPolicy);//* //AutoAppConfigTest에서 확인 가능

        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

//    @Autowired //필드명 매칭법
//    private DiscountPolicy rateDiscountPolicy;

    //@Qualifier("maindiscountPolicy") DiscountPolicy discountPolicy
    // 생성자에서 qualifier 주입


    /*
    생성자 주입 방식을 선택하는 이유?!
    프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법
    기본적으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다.
    생성자 주입과 수정자 주입은 동시에 사용할 수 있음
    항상 생성자 주입을 선택해! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해. 필드주입은 쓰지마.
     */

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member,itemPrice);

        return new Order(memberId,itemName,itemPrice,discountPrice);
    }

    //Test용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
