package java.io.reflectoring.buckpal.adaptor.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.reflectoring.buckpal.application.port.in.SendMoneyCommand;
import java.io.reflectoring.buckpal.application.port.in.SendMoneyUseCase;
import java.io.reflectoring.buckpal.domain.Account.AccountId;
import java.io.reflectoring.buckpal.domain.Money;

@RestController
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    public SendMoneyController(SendMoneyUseCase sendMoneyUseCase) {
        this.sendMoneyUseCase = sendMoneyUseCase;
    }

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    public void sendMoney(@PathVariable("sourceAccountId") Long sourceAccountId,
                          @PathVariable("targetAccountId") Long targetAccountId,
                          @PathVariable("amount") Long amount) {
        SendMoneyCommand command = new SendMoneyCommand(
                new AccountId(sourceAccountId),
                new AccountId(targetAccountId),
                Money.of(amount)
        );

        sendMoneyUseCase.sendMoney(command);
    }

}
