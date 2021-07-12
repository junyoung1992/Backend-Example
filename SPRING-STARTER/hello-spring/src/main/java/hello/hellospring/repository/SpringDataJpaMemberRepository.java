package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface는 extends 사용
 * JpaRepository<T, ID>
 */
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    // 단순한 메서드는 인터페이스만으로도 자동화
    // JPQL: SELECT m FROM Member m WHERE m.name = ?
    @Override
    Optional<Member> findByName(String name);
}
