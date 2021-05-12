package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("Koalal");
        Member savedMember = memberRepository.save(member);

        // Optional<Member> byId = memberRepository.findById(savedMember.getId());  // or
        Member findMember = memberRepository.findById(savedMember.getId()).get();  // 단, Exception 가능
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        System.out.println(findMember + "  ; findMember");
        System.out.println(member + "  ; member");
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member 1");
        Member member2 = new Member("member 2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long countAfterDelete = memberRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndGreaterThan() {
        Member member1 = new Member("Cruella", 10);
        Member member2 = new Member("Cruella", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("Cruella", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("Cruella");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testFindById() {
        Member member1 = new Member("member 1");
        memberRepository.save(member1);

        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        Optional<Member> byId = memberRepository.findById(member1.getId());
        if(byId.isEmpty()) {
            System.out.println("(Not Found) by isEmpty()");
        } else {
            System.out.println(byId.get());
        }
    }

    @Test
    public void testNamedQuery() {
        Member member1 = new Member("Cruella", 40);
        Member member2 = new Member("Dalmatian", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("Cruella");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("Cruella", 40);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findUser("Cruella", 40);
        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    public void testFindUsernameList() {
        Member member1 = new Member("Cruella", 40);
        Member member2 = new Member("Dalmatian", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("testFindUsernameList() >> s = " + s);
        }
    }

    @Test
    public void testMemberDto() {
        Team team = new Team( "Team X");
        teamRepository.save(team);

        Member member1 = new Member("Cruella", 40);
        Member member2 = new Member("Dalmatian", 20);
        member1.setTeam(team);
        member2.setTeam(team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        for (MemberDto memberDto : memberDtoList) {
            System.out.println("[testMemberDto()] >> memberDto = " + memberDto);
        }
    }

    @Test
    public void testFindByNames() {
        Member member1 = new Member("Cruella", 40);
        Member member2 = new Member("Dalmatian", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> names = new ArrayList<>();
        names.add("Cruella");
        names.add("Dalmatian");
        names.add("NoName");
        List<Member> memberList = memberRepository.findByNames(names);
        for (Member member : memberList) {
            System.out.println("◆ member = " + member);
        }

        List<Member> result = memberRepository.findByNames(Arrays.asList("Talmatian", "Cruella"));
        for (Member member : result) {
            System.out.println("■ member = " + member);
        }
    }

    @Test
    public void testReturnType() {
        Member member1 = new Member("Cruella", 40);
        Member member2 = new Member("Cruella", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findListByUsername("Cruella");
        //0개일 경우 null아닌 empty (빈 컬렉션)을 반환
        System.out.println(">> result.size() = " + result.size());

        Member aMember = memberRepository.findMemberByUsername("Cruella");
        System.out.println(">> aMember = " + aMember);

        Optional<Member> aOpt = memberRepository.findOptionalByUsername("Cruella");
        System.out.println(">> aOpt = " + aOpt);
    }


    //-- 17강 스프링 데이터 JPA 페이징과 정렬
    @Test
    public void page() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));

        int age = 10;

        //When
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //Then
        List<Member> content = page.getContent();  // 조회된 데이터
        /*long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);*/

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(7);
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();  //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue();  //다음페이지가 있는가?

    }

    // 18강 벌크성 수정 쿼리
    @Test
    @Rollback(false)
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 55));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush(); em.clear();

        //
        Member member5 = memberRepository.findByUsername("member5").get(0);
        System.out.println("member5 = " + member5);

        // Then
        assertThat(resultCount).isEqualTo(3);
    }


    // 19강 @EntityGraph
    @Test
    public void                                                                                                                                                                                                                                                                                                                                                                                                                                                                         findMemberLazy() {
        //Given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamB));

        em.flush(); em.clear();

        //When
        //List<Member> members = memberRepository.findMemberFetchJoin(); // fetch join 호출 예제
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        //List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }


    // 20강 JPA Hint & Lock          // read-only query ?
    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //When
        //Member findMember = memberRepository.findById(member1.getId()).get();  // Dirty Checking이 됨 (내부적으로 비교용 원본(스냅샷이라 하지)을 보존함)
        Member findMember = memberRepository.findReadOnlyByUsername("member1");   // Dirty chk용 snapshot 없음. 즉, D-check 안됨.
                    // 즉, 아래 flush()에서 Update 쿼리 안 나감
        findMember.setUsername("member2");
        em.flush();

    }


    //-- 21강 사용자 정의 리포지토리 구현
    @Test
    public void callCustom() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        List<Member> result = memberRepository.findMemberCustom();
        for (Member member : result) {
            System.out.println(">>> member = " + member);
        }
    }

    //-- 22강 Auditing  // 여기는 study.datajpa.repository.MemberRepositoryTest 임
    @Test
    public void JpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1");
        memberRepository.save(member);  // --> @PrePersist

        Thread.sleep(2000L);
        member.setUsername("member2");  // --> @PreUpdate
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("findMember.getCreateDate() = " + findMember.getCreateDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getUpdateDate());
    }

}
