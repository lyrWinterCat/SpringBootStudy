package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") //주인이 아님. 연관관계의 거울이다 라는 표현 mapped by (order.class의 member)를 통해 결정될거야.
    private List<Order> orders = new ArrayList<>(); //가장 좋은 방법
    // 컬렉션은 필드에서 초기화 (안전!) + 하이버네이트가 관리하기 편함

}
