package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User : Post -> 1 : (0~N), Main : Sub -> Parent : Child
    // Post 입장에서는 User 객체는 하나만 와야 함
    // User 는 여러 Post 에 들어갈 수 있음
    // 한 명의 사용자가 여러 개의 글을 작성할 수 있음 -> 1 대 다 관계
    // LAZY: 지연 로딩 방식
    // user 엔티티가 조회될 때 항상 post 엔티티가 조회되는 것이 아니라
    // post 데이터가 로딩되는 시점에 필요한 user 데이터를 가져옮
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

}
