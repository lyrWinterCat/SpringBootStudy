package hello.core.scan.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.context.annotation.ComponentScan.*;

public class ComponentFilterAppConfigTest {
    @Test
    void filterScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        //ac.getBean("beanB",BeanB.class);
        assertThrows(NoSuchBeanDefinitionException.class,()->ac.getBean("beanB",BeanB.class));

    }

    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type= FilterType.ANNOTATION,classes = MyIncludeComponent.class),
            excludeFilters = @Filter(classes= MyExcludeComponent.class) //type은 default값이기 때문에 생략가능
            // beanA를 빼고싶다면 excludeFilters에 type=FilterType.ASSOGNABLE_TYPE, classes=BeanA.class 를 추가해 주면 된다
    )
    static class ComponentFilterAppConfig{

    }


}
