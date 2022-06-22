package java.io.reflectoring.buckpal.application.service;

import lombok.RequiredArgsConstructor;

import java.io.reflectoring.buckpal.application.port.in.GetAccountBalanceQuery;
import java.io.reflectoring.buckpal.application.port.out.LoadAccountPort;
import java.io.reflectoring.buckpal.domain.Account.AccountId;
import java.io.reflectoring.buckpal.domain.Money;
import java.time.LocalDateTime;

@RequiredArgsConstructor
class GetAccountBalanceService implements GetAccountBalanceQuery {

	private final LoadAccountPort loadAccountPort;

	@Override
	public Money getAccountBalance(AccountId accountId) {
		return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
				.calculateBalance();
	}

}
