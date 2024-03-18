package datajpa.study.repository;

import datajpa.study.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;
    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public void delete(Member member){
        em.remove(member);
    }
    public List<Member> findAll(){
        //전체 조회는 jpaql를 사용해야한다
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    public Optional<Member> findById(Long id){
        Member member=em.find(Member.class,id);
        return Optional.ofNullable(member);
        //널을 허용한다
        //of는 널을 혀용하지 않는다
    }
    public long count(){
        return em.createQuery("select count(m) from Member m",Long.class)
                .getSingleResult();
    }
    public Member find(Long id){
        return em.find(Member.class,id);
    }
    public List<Member> findByUsernameAndAge(String username,int age){//age가 기존의 age이상이면
        return em.createQuery("select m from Member m where m.username=:username and m.age >:age")
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();
    }

    public List<Member> findByPage(int age,int offset,int limit){
        //나이가 10이면서 이름으로 내림 차순
        return em.createQuery("select m from Member m where m.age=:age order by m.username desc ")
                .setParameter("age",age)
                .setFirstResult(offset)//시작 위치
                .setMaxResults(limit)//최대
                .getResultList();
    }
    public long totoalCount(int age){//페이지 카운트 반환
        return em.createQuery("select count(m) from Member m where m.age=:age", Long.class)
                .setParameter("age",age)
                .getSingleResult();
    }
    public int bulkAgePlus(int age){//age 같거나 크면 1+
        return em.createQuery("update Member m set m.age=m.age+1 where m.age>=:age")
                .setParameter("age",age)
                .executeUpdate();
    }
}
