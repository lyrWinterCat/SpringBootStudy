package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //일대다 매핑
    private List<Order> orders = new ArrayList<>(); //가장 좋은 방법
    // 컬렉션은 필드에서 초기화 (안전!) + 하이버네이트가 관리하기 편함

}
