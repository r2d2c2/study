package datajpa.study.repository;

import datajpa.study.dto.MemberDto;
import datajpa.study.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> ,MemberRepositoryCustom{
    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);
    //이름이 같고 age가 일정 값이상이면 반환
    List<Member> findHelloBy();
    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //이름과 나이로 검색하여 조회
    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findUser(@Param("username")String username,@Param("age")int age);

    //유저 네임만 모두 받기
    @Query("select m.username from Member m")
    List<String> findUsernameList();


    @Query("select new datajpa.study.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names")List<String> names);

    List<Member> findListByUsername(String username);//컬랙션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionalByUsername(String username);//단건 옵션


    Page<Member> findByAge(int age, Pageable pageable);
    //age로 페이지 찾기
    //Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age=m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age")int age);

    //N+1 문제 해결
    @Query("select m from Member m left join fetch m.team")
    //Member와 연관된 team을 모두 불러오기
    List<Member> findmemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);


    List<UsernameOnly> findProjectionsByUsername(String username);


    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);


}
