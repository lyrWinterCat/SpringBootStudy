package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;


/*
프록시 스코프 사용 => Provider를 쓴 것과 동일한 효과
적용 대상이 인터페이스가 아닌 클래스라면 target_class
인터페이스라면 interface 선택
>>  MyLogger의 가짜 프록시 클래스를 만들어두고 http request와 상관 없이 가짜 프록시 클래스를
다른 빈에 미리 주입해 둘 수 있다.
>> CGLIB이라는 라이브러리로 내 클래스에 상속받은 가짜 프록시 객체를 만들어서 주입 / 클라이언트 입장에서는 원본인지 아닌지도 모름
동이하게 사용할 수 있음(다형성)

동작 정리
CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
이 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직이 들어있다.
가짜 프록시 객체는 실제 request scope와는 관계가 없다. 그냥 가짜이고, 내부에 단순한 위임 로직만
있고, 싱글톤 처럼 동작한다.

특징 정리
프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수
있다.
사실 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지
지연처리 한다는 점이다.
단지 애노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI
컨테이너가 가진 큰 강점이다.
꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다.

** 마치 싱글톤을 사용하는것 같지만 다르게 동작함 >> 주의 !
꼭 필요한 곳에 최소화해서 사용. (유지보수를 위해)

*/


@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("["+uuid+"]"+"["+requestURL+"] "+message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("["+uuid+"] request scope bean create:"+this);
    }

    @PreDestroy
    public void close(){
        System.out.println("["+uuid+"] request scope bean close:"+this);
    }
}
