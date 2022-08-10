package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    //일대일 매핑 (여기가 거울 관계. order의 delivery를 통해 값을 받겠다)
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    //Enum 타입 클래스를 가져올 때 기본설정
    @Enumerated(EnumType.STRING) //ordinal : 숫자로 지정. 나중에 글자 밀리면 대참사 일어남. 꼭 string으로!
    private DeliveryStatus status; //ready, comp

}
