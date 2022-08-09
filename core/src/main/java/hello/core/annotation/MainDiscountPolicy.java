package hello.core.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
    //직접 어노테이션 만들기 ^^
    /*
    어노테이션은 상속 기능이 없음.
    여러 어노테이션을 모아서 사용하는 기능은 스프링이 지원
    @Qualifier 뿐만 아니라 다른 어노테이션도 함께 조합해서 사용할 수 있으며,
    @Autowired도 재정의 할 수 있다.
    그러나 무분별하게 재정의 하는 것은 유지보수에 혼란을 줄 수 있다.

     */
}
