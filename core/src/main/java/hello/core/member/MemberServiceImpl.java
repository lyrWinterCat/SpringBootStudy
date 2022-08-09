package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //ctrl shift enter >> ; 까지 자동완성됨
    private final MemberRepository memberRepository; //선언만 하고 생성자 생성

    @Autowired //ac.getBan(MemberRepository.class)
    public MemberServiceImpl(MemberRepository memberRepository) {//생성자 주입을 위한 생성자
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberid) {
        return memberRepository.findById(memberid);
    }

    //테스트 용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }

}
