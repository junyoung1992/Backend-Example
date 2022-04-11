package me.whiteship.inflearnthejavatest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Study {

    @Id @GeneratedValue
    private Long id;
    private StudyStatus status = StudyStatus.DRAFT;
    private int limitCount;
    private String name;
    private LocalDateTime openedDateTime;
    private Long ownerId;

    public Study(int limitCount) {
        if (limitCount < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야 한다.");
        }
        this.limitCount = limitCount;
    }

    public Study(int limitCount, String name) {
        this.limitCount = limitCount;
        this.name = name;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public void open() {
        this.openedDateTime = LocalDateTime.now();
        this.status = StudyStatus.OPENED;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limitCount=" + limitCount +
                ", name='" + name + '\'' +
                '}';
    }

}
