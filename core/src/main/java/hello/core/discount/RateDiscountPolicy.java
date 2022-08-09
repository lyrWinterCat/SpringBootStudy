package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Qualifier("mainDiscountPolicy")
//@Primary
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP){
            return price * discountPercent/100;
        }
        return 0;
    }
}

/*
조회 대상 bean이 2개 이상일 떄 해결법
1. @Autowired 필드명 매칭
: 필드명을 빈 이름으로 변경
>> 타입 매칭 / 타입 매칭의 결과가 2개 이상일 때 필드명, 파라미터 명으로 빈 이름 매칭

2. @Qualifier 사용
 : 추가 구분자 붙여주기. (빈이름 변경이 아님)
 ** @Bean 직접 등록 시에도 Quilifier 사용 가능

 3. @Primary 사용
 : 우선순위 지정. 해당 어노테이션이 붙은 빈이 우선권을 가진다.

 ** primary? qualifier?
 스프링은 자동보다 수동이, 넓은 범위의 선택권보다는 좁은 범위의 선택권이 우선순위가 높음
 따라서 qualifier가 보다 우선권이 있음

 */
