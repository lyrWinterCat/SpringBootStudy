package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@AllArgsConstructor 보다 한단계 더 나은 어노테이션이 @RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

   // @Autowired >> 필드주입법 - 테스트할때나 쓰지 실제로 쓰면 곤란...
    private final MemberRepository memberRepository; //final > compile시 한번 더 확인 가능. 되도록이면 final로 선언

//    @Autowired //setter 주입법 - 테스트 시 mock 주입 가능 (변경 가능, 가짜 memberRepository)
//    그러나 보통 애플리케이션 조립시점에 setting이 완료되기 때문에 바꿀 일은 별로 없음. (별로 좋지 않은 방법)
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //@Autowired //생성자 주입법 >> 가장 많이 사용함, 생성자가 하나라면 자동주입됨 + lombok으로 생략가능
    // 테스트 케이스에서 편리. (Mock을 놓치지 않고 주입 가능)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원 가입
    @Transactional //default인 readOnly = false가 걸림
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 중복실행 시 에러의 가능성이 있으므로, 안전하게 가야 한다.
    // 회원 테이블의 회원명 컬럼에 유니크 제약 조건 추가
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    //@Transactional(readOnly = true) //읽기에는 readOnly true를 넣어주는 것이 좋음
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
