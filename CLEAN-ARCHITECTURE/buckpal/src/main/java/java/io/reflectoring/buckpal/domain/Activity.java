package java.io.reflectoring.buckpal.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.io.reflectoring.buckpal.domain.Account.AccountId;
import java.time.LocalDateTime;

public final class Activity {

    @NonNull
    private ActivityId id;

    @NonNull
    private final AccountId ownerAccountId;

    @NonNull
    private final AccountId sourceAccountId;

    @NonNull
    private final AccountId targetAccountId;

    @NonNull
    private final LocalDateTime timestamp;

    @NonNull
    private final Money money;

    public ActivityId getId() {
        return id;
    }

    public AccountId getOwnerAccountId() {
        return ownerAccountId;
    }

    public AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Money getMoney() {
        return money;
    }

    public Activity(
            @NonNull AccountId ownerAccountId,
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull Money money) {
        this.id = null;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

    public Activity(
            @NonNull ActivityId id,
            @NonNull AccountId ownerAccountId,
            @NonNull AccountId sourceAccountId,
            @NonNull AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull Money money) {
        this.id = id;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

    public static final class ActivityId {
        private final Long value;

        public ActivityId(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }
    }

}
