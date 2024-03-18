package datajpa.study.controller;


import datajpa.study.dto.MemberDto;
import datajpa.study.entity.Member;
import datajpa.study.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();//userA
    }
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id")Member member){
        return member.getUsername();//userA
    }
    //권장 하지는 않으나 간단한 조회용으로 사용시 편리

    @GetMapping("/memebers")
    public Page<MemberDto> list(@PageableDefault(size = 5,sort = "username") Pageable pageable){
        Page<MemberDto> page = memberRepository.findAll(pageable)
                .map(MemberDto::new);
        //Member를 MemberDto로 변경
        return page;
    }

    //데이터 초기값 주기
    /*@PostConstruct
    public void init(){//초기값 저장 id값이 1로 저장
//        memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }
    }*/
}
