package datajpa.study.repository;

import datajpa.study.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member=new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());
        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(saveMember).isEqualTo(findMember);
    }
    @Test
    public void basicCRUD() throws Exception{
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        Member findmember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findmember2 = memberJpaRepository.findById(member2.getId()).get();

        //then 단건
        assertThat(findmember1).isEqualTo(member1);
        assertThat(findmember2).isEqualTo(member2);

        //리스트 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //카운트
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);
        findmember1.setUsername("이름 변경!!!");
        //변경 감지로 알아서 변경

        //삭제
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }
    @Test
    public void findByUsernameAndAgeTest() throws Exception{
        //given
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAge("aaa", 15);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void paging() throws Exception{
        //given
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        //when
        int age=10;
        int offset=0;
        int limit=3;
        List<Member> mambers = memberJpaRepository.findByPage(age, offset, limit);
        long totoalCount = memberJpaRepository.totoalCount(age);

        //then
        assertThat(mambers.size()).isEqualTo(3);
        assertThat(totoalCount).isEqualTo(5);
    }
    @Test
    public void bulkUpdate() throws Exception{
        //given
        memberJpaRepository.save(new Member("memeber1",10));
        memberJpaRepository.save(new Member("memeber2",19));
        memberJpaRepository.save(new Member("memeber3",20));
        memberJpaRepository.save(new Member("memeber4",21));
        memberJpaRepository.save(new Member("memeber5",40));
        //when
        int i =memberJpaRepository.bulkAgePlus(20);
        //20이상 인 것들

        //then
        assertThat(i).isEqualTo(3);
    }
}