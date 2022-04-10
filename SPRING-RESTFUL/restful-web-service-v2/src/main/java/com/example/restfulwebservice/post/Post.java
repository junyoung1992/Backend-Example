package com.example.restfulwebservice.post;

import com.example.restfulwebservice.user.User;
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
    private Long id;

    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)  // USER : POST -> 1 : N
    private User user;

}
