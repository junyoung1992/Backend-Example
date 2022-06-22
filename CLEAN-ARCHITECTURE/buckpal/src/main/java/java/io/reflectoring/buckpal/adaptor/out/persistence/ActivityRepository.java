package java.io.reflectoring.buckpal.adaptor.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

    @Query("""
        SELECT a
        FROM ActivityJpaEntity a
        WHERE a.ownerAccountId = :owner
        AND a.timestamp >= :since
        """)
    List<ActivityJpaEntity> findByOwnerSince(@Param("owner") Long ownerAccountId, @Param("since") LocalDateTime since);
//    List<ActivityJpaEntity> findByOwnerAccountIdAndTimestampGreaterThanEqual(Long ownerAccountId, LocalDateTime since);

    @Query("""
        SELECT sum(a.amount)
        FROM ActivityJpaEntity a
        WHERE a.targetAccountId = :accountId
        AND a.ownerAccountId = :accountId
        AND a.timestamp < :until
        """)
    Long getDepositBalanceUntil(@Param("accountId") Long accountId, @Param("until") LocalDateTime until);

    @Query("""
        SELECT sum(a.amount)
        FROM ActivityJpaEntity a
        WHERE a.sourceAccountId = :accountId
        AND a.ownerAccountId = :accountId
        AND a.timestamp < :until
        """)
    Long getWithdrawalBalanceUntil(@Param("accountId") Long accountId, @Param("until") LocalDateTime until);

}
