package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member); // 무슨 의미인가?
    }

    public List<Member> findAll() {
        return em.createQuery("Select m From Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("Select Count(m) From Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("Select m From Member m WHERE m.username = :username And m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }


    // 16강 순수 JPA 페이징과 정렬
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("Select m From Member m Where m.age = :age Order By m.username Desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("Select Count(m) From Member m Where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }


    // 18강 벌크성 수정 쿼리
    public int bulkAgePlus(int age) {
        return em.createQuery("Update Member m Set m.age = m.age + 1 Where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
