package io.spring.springbatchlecture.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer2 {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String birthdate;

}
