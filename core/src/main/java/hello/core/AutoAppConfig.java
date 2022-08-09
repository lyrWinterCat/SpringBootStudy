package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( //component가 붙은 모든 객체를 스캔할건데, exclude: 제외할 것 설정
        //@Bean 설정을 안해주어도 됨
        basePackages = "hello.core.member", //탐색할 패키지의 시작 위치 지정. 없다면 전체 스캔
        basePackageClasses = AutoAppConfig.class, //해당 클래스가 속한 패키지부터 스캔
        excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION,classes=Configuration.class)
)
public class AutoAppConfig { //만약 basePackage를 설정하지 않는다면, 해당 클래스가 포함된 패키지부터 스캔하기 시작
    // 권장 : 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것.
    /* 예시
    com.hello
    com.hello.service
    com.hello.repository
    >> com.hello >> 프로젝트 시작 루트. 여기에 AppConfig같은 메인 설정 정보를 두고, ComponentScan 어노테이션을 붙인 후
    basePackages 지정은 생략
     */

    /*@Bean(name="memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }*/


}
