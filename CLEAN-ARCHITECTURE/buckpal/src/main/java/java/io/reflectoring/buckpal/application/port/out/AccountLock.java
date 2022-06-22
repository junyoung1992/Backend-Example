package java.io.reflectoring.buckpal.application.port.out;

import static java.io.reflectoring.buckpal.domain.Account.*;

public interface AccountLock {

	void lockAccount(AccountId accountId);

	void releaseAccount(AccountId accountId);

}
