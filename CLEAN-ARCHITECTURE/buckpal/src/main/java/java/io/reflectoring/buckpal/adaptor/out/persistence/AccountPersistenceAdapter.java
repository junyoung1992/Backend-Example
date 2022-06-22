package java.io.reflectoring.buckpal.adaptor.out.persistence;

import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.reflectoring.buckpal.application.port.out.LoadAccountPort;
import java.io.reflectoring.buckpal.application.port.out.UpdateAccountStatePort;
import java.io.reflectoring.buckpal.domain.Account;
import java.io.reflectoring.buckpal.domain.Account.AccountId;
import java.io.reflectoring.buckpal.domain.Activity;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    public AccountPersistenceAdapter(AccountRepository accountRepository, ActivityRepository activityRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
        AccountJpaEntity account = accountRepository.findById(accountId.getValue())
                .orElseThrow(EntityNotFoundException::new);

        List<ActivityJpaEntity> activities = activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);

        Long withdrawalBalance = orZero(activityRepository.getWithdrawalBalanceUntil(accountId.getValue(), baselineDate));

        Long depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));

        return accountMapper.mapToDomainEntity(
                account,
                activities,
                withdrawalBalance,
                depositBalance
        );
    }

    @Override
    public void updateActivities(Account account) {
        for (Activity activity : account.getActivityWindow().getActivities()) {
            if (activity.getId() == null) {
                activityRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }

    private Long orZero(Long value) {
        return value == null ? 0L : value;
    }
}
