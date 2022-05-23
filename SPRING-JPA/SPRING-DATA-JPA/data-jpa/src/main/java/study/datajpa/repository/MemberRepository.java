package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);   // Collection
    Member findMemberByUsername(String username);   // 단건
    Optional<Member> findOptionalByUsername(String username);   // Optional

    /*
     * Paging
     */
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t where m.age = :age",
            countQuery = "select count(m) from Member m")
    Page<Member> findPageByAge(@Param("age") int age, Pageable pageable);

    /*
     * Bulk Execution
     */
    @Modifying(clearAutomatically = true)  // modifying 이 있어야 jpa 에서 executeUpdate 를 실행한다
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /*
     * fetch join - @EntityGraph
     */
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    /*
     * Query Hint
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<Member> findReadOnlyById(long id);

    /*
     * Lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    /*
     * Projections
     */
    List<UsernameOnly> findProjectionsByUsername(String username);
    List<UsernameOnlyDto> findProjectionsDtoByUsername(String username);
    <T> List<T> findProjectionsDtoByUsername(String username, Class<T> type);

    /*
     * Native Query
     */
    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);
    @Query(value = "select m.member_id as id, m.username, t.name as teamName from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
