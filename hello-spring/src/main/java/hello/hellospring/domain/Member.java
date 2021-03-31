package hello.hellospring.domain;

import javax.persistence.*;

// JPA에서 Entity 관리
@Entity
public class Member {

    // DB가 알아서 생성해주는 것을 Identity라고 함
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
