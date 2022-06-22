package java.io.reflectoring.buckpal.application.port.out;

import java.io.reflectoring.buckpal.domain.Account;
import java.io.reflectoring.buckpal.domain.Account.AccountId;
import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);

}
