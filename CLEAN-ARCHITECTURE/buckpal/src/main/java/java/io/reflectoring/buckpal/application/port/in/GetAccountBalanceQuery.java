package java.io.reflectoring.buckpal.application.port.in;

import java.io.reflectoring.buckpal.domain.Money;

import static java.io.reflectoring.buckpal.domain.Account.AccountId;

public interface GetAccountBalanceQuery {

	Money getAccountBalance(AccountId accountId);

}
