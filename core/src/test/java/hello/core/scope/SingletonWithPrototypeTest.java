package hello.core.scope;

import ch.qos.logback.core.net.server.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest {
    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

    }

    @Scope("singleton")
    static class ClientBean{
        //private final PrototypeBean prototypeBean; //생성시점에 주입 x01

//        @Autowired >> 단순한 방법. logic이 실행될때마다 protobean 생성
//        ApplicationContext applicationContext;

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider; //  JSR-330 Provider >> 자바 기반 provider/ 장 : 심플, 스프링이 아닌 다른 컨테이너에서도 사용 가능 / 단 : 심플, 별도의 라이브러리 필요
        //private ObjectFactory<PrototypeBean> prototypeBeanProvider; // 단순한 기능, 별도의 라이브러리 필요 없음, 스프링에 의존
        //private ObjectProvider<PrototypeBean> prototypeBeanProvider; //Factory 상속, 옵션, 스트림 처리 등 편의기능 존재, 라이브러리필요없음, 스프링 의존

        // 어떤 기능을 사용할지는 본인의 선택


//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean){
//
//            this.prototypeBean = prototypeBean;
//        }

        public int logic(){
            //PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class); //단순한 방법
            //PrototypeBean prototypeBean = prototypeBeanProvider.getObject(); //찾아주는 기능만 사용 >>DL

            PrototypeBean prototypeBean = prototypeBeanProvider.get(); // Provider inject 사용
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }
    /*
    * 의존관계를 외부에서 주입(DI)받는게 아니라 직접 필요한 의존관계를 찾는 것을 Dependency Lookup(DL) 의존관계 조회(탐색)
    * >> DL 기능만 제공하는 무언가가 필요 >> ObjectProvider  ObjectFactory
    *>> 항상 새로운 프로토타입 빈이 생성됨
    * */




    /*@Scope("singleton")
    static class ClientBean2{
        private final PrototypeBean prototypeBean; //생성시점에 주입 X 02


        @Autowired
        public ClientBean2(PrototypeBean prototypeBean){
            this.prototypeBean = prototypeBean;
        }
        public int logic(){
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }*/


    @Scope("prototype")
    static class PrototypeBean{
        private int count=0;
        public void addCount(){
            count++;
        }
        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init " +this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
