package hello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient //implements InitializingBean, DisposableBean
{
    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출, url= "+url);

    }

    public void setUrl(String url){
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){

        System.out.println("call: "+url+ " message = " + message);
    }

    //서비스 종료시 호출
    public void disconnect(){
        System.out.println("close : "+url);
    }

    @PostConstruct
    public void init(){
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close(){
        System.out.println("NetworkClient.close");
        disconnect();
    }
    /*
    @PostConstruct, @PreDestroy
     가장 추천하는 최신 기술
     컴포넌트 스캔과 잘 어울림
     유일한 단점 : 외부 라이브러리에는 적용하지 못함 >> init,destroy 메서드 사용
    */



    
/*    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }
    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();
    }*/
    
    /*빈 등록시 메서드 넣기 @Bean(initMethod = "init",destroyMethod = "close")
    등록할 때 메서드 이름을 자유롭게 줄 수 있음
    스프링빈이 스프링코드에 의존하지 않음
    코드가 아니라 설정정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있음
    * 
    
    ** destroyMethod 의 default : (inferred) / 추론
    이 추론기능은 close, shutdown라는 이름의 메서드를 자동으로 호출
    >> 직접 스프링 빈으로 등록하면, 종료 메서드는 따로 적어주지 않아도 잘 동작함
    >> 추론기능을 사용하기 싫다면 destroyMethod=""로 빈 공백을 지정하면 됨  
    
    +autoClose 기능도 알아두면 좋음
    * */



    //의존관계 연결 끝나고 호출
/*    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메세지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }*/
    /*  implements InitializingBean, DisposableBean
    초기화 , 소멸 인터페이스 단점
    이 인터페이스는 스프링 전용 인터페이스.
    초기화, 소멸 메서드명 변경 불가
    내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없음
     >> 스프링 초창기 방법. 지금은 더 나은 방법이 있기 때문에 거의 사용하지 않음

     */


}
