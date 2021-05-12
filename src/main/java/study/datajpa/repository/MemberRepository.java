package study.datajpa.repository;

/* Spring Data JPA    Repository */


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //findTop3HelloBy();

    //-- 11강 JPA Named Query  --> domain.Member 에 쿼리 정의됨 @NamedQuery
    //@Query(name = "Member.findByUsername")
    public List<Member> findByUsername(@Param("username") String username);

    //-- 12강 @Query, 리포지토리 메소드에 쿼리 정의하기
    @Query("Select m From Member m Where m.username = :username And m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //-- 13강 @Query, 값, DTO 조회하기
    @Query("Select m.username From Member m")
    List<String> findUsernameList();

    @Query("Select new study.datajpa.MemberDto(m.id, m.username, t.name) From Member m Join m.team t")
    List<MemberDto> findMemberDto();

    //-- 14강 파라미터 바인딩
    @Query("Select m From Member m Where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //-- 15강 반환 타입
    List<Member> findListByUsername(String username);   // Collection
    Member findMemberByUsername(String username);    // 단건
    Optional<Member> findOptionalByUsername(String username);  // 단건 Optional


    //-- 17강 스프링 데이터 JPA 페이징과 정렬
    Page<Member> findByAge(int age, Pageable pageable);
    //Slice<Member> findByAge(int age, Pageable pageable);
    //List<Member> findByAge(int age, Pageable pageable);
    // method name으로 쿼리 만드는 거


    // 18강 벌크성 수정 쿼리
    @Modifying(clearAutomatically = true)
    @Query("Update Member m Set m.age = m.age + 1 Where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // 19강 @EntityGraph
    @Query("Select m From Member m Left Join Fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})        // --> 사실상 fetch join 기능을 쓰는 것이다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("Select m From Member m")
    List<Member> fineMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    // 20강 JPA Hint & Lock
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
