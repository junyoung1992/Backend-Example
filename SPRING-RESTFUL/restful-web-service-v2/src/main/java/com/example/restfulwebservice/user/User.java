package com.example.restfulwebservice.user;

import com.example.restfulwebservice.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonIgnoreProperties(value={"password", "ssn"})
// @JsonFilter("UserInfo")
@Schema(description="사용자 상세 정보를 위한 도메인 객체")
@Entity
public class User {

    @Id // id 값 지정
    @GeneratedValue // id 자동 증가
    private Long id;

    // @JsonIgnore
    @Schema(description="사용자의 비밀번호를 입력해주세요.")
    private String password;

    // @JsonIgnore
    @Schema(description="사용자의 주민번호를 입력해주세요.")
    private String ssn;

    @Size(min=2, message="Name은 2자 이상 입력해주세요.")    // 최소 2글자
    @Schema(description="사용자의 이름을 입력해주세요.")
    private String name;

    @Past   // 과거 날짜만 가능
    @Schema(description="사용자의 등록일을 입력해주세요.")
    private Date joinDate;

    @OneToMany(mappedBy = "user")  // USER : POST -> 1 : N
    private List<Post> posts;

    public User(Long id, String password, String ssn, String name, Date joinDate) {
        this.id = id;
        this.password = password;
        this.ssn = ssn;
        this.name = name;
        this.joinDate = joinDate;
    }

}
