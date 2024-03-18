package datajpa.study.repository;

import datajpa.study.dto.MemberDto;
import datajpa.study.entity.Member;
import datajpa.study.entity.Team;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired EntityManager entityManager;

    @Test
    public void testMember(){
        Member member=new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(saveMember.getId());
        assertThat(byId.get().getId()).isEqualTo(saveMember.getId());
        assertThat(byId.get().getUsername()).isEqualTo(saveMember.getUsername());
        assertThat(byId.get()).isEqualTo(saveMember);
    }
    @Test
    public void basicCRUD() throws Exception{
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findmember1 = memberRepository.findById(member1.getId()).get();
        Member findmember2 = memberRepository.findById(member2.getId()).get();

        //then 단건
        assertThat(findmember1).isEqualTo(member1);
        assertThat(findmember2).isEqualTo(member2);

        //리스트 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //카운트
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
        findmember1.setUsername("이름 변경!!!");
        //변경 감지로 알아서 변경

        //삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }
    @Test
    public void findByUsernameAndAgeTest() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void findHelloBy() throws Exception{
        List<Member> byHelloBy = memberRepository.findHelloBy();
        List<Member> byHelloBy2 = memberRepository.findTop3HelloBy();
    }
    @Test
    public void testNamedQurey() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<Member> result = memberRepository.findByUsername("aaa");
        Member findMember = result.get(0);
        //then
        assertThat(findMember).isEqualTo(m1);
    }
    @Test
    public void testQurey() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<Member> result = memberRepository.findUser("aaa",10);
        //then
        assertThat(result.get(0)).isEqualTo(m1);
    }
    @Test
    public void testQurey2() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("bbb", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        //when
        List<String > result = memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void testQurey3() throws Exception{
        //given
        Team team=new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("aaa", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        //when
        List<MemberDto> result = memberRepository.findMemberDto();
        for (var s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void testQurey4() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("bbb", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("aaa","bbb"));
        for (var s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void returnType() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("aaa");
        //없으면 [] 단 중복 값을 처리 가능
        Member aaa1 = memberRepository.findMemberByUsername("aaaa");
        //없으면 null
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("aaaa");
        //없으면 Optional.empty 단 중복값 처리 불가
        System.out.println("aaa = " + aaa);
        System.out.println("aaa1 = " + aaa1);
        System.out.println("aaa2 = " + aaa2);
    }
    @Test
    public void paging() throws Exception{
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        //when
        int age=10;

        PageRequest pageable = PageRequest.
                of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //0부터 3까지 이름 내림 차순
        Page<Member> page = memberRepository.findByAge(age,pageable);
        //Slice<Member> page2 = memberRepository.findByAge2(age,pageable);

        Page<MemberDto> tomap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        //dto변환 안전하게 api로 사용가능

        List<Member> content=page.getContent();
        long totalElements=page.getTotalElements();//없는 기능

        System.out.println("totalElements = " + totalElements);
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        //then
        assertThat(content.size()).isEqualTo(3);//조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5);//전체 데이터수
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 번호
        assertThat(page.isFirst()).isTrue();//첮번째 페이지 인가
        assertThat(page.hasNext()).isTrue();//다음 페이지가 있냐

    }

    @Test
    public void bulkUpdate() throws Exception{
        //given
        memberRepository.save(new Member("memeber1",10));
        memberRepository.save(new Member("memeber2",19));
        memberRepository.save(new Member("memeber3",20));
        memberRepository.save(new Member("memeber4",21));
        memberRepository.save(new Member("memeber5",40));
        //when
        int i =memberRepository.bulkAgePlus(20);
        //20이상 인 것들
        //하지만 데이터베이스에 반영 되지 않았다
        //entityManager.flush();//반영
        //entityManager.clear();//데이터베이스 정리

        List<Member> member5 = memberRepository.findByUsername("memeber5");
        Member member=member5.get(0);//이름이 동일한 것중 처번째
        System.out.println("member = " + member);
        //age가 +1되어 Member(id=5, username=memeber5, age=41)출력

        //then
        assertThat(i).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception{
        //given
        Team teamA=new Team("teamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        //when
        List<Member> membera = memberRepository.findmemberFetchJoin();
        for (Member member : membera) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void queryHint() throws Exception{
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        entityManager.flush();
        entityManager.clear();
        //when
        var findMember = memberRepository.findLockByUsername("member1");
        //findMember.setUsername("asdfasdf2");
        //락으로 잠겨 있어 사용 불가
        entityManager.flush();
        //then
    }
    @Test
    public void callCustom() throws Exception{
        //given
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }
    @Test
    public void basic() throws Exception{
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);
        entityManager.persist(new Member("m1", 0, teamA));
        entityManager.persist(new Member("m2", 0, teamA));
        entityManager.flush();
        //when
        //Probe 생성
        Member member = new Member("m1");
        Team team = new Team("teamA"); //내부조인으로 teamA 가능
        member.setTeam(team);
        //ExampleMatcher 생성, age 프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);
        //then
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void projections() throws Exception{
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        entityManager.persist(m1);
        entityManager.persist(m2);
        entityManager.flush();
        entityManager.clear();
        //when
        List<UsernameOnly> result =
                memberRepository.findProjectionsByUsername("m1");
        //then
        Assertions.assertThat(result.size()).isEqualTo(1);

    }
}