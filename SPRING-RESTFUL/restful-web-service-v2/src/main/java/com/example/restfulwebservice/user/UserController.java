package com.example.restfulwebservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor // final이나 @NonNull이 붙은 변수에 대해 생성자를 생성
public class UserController {

    private final UserDaoService service;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/1 or /users/10 -> String
    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> retrieveUser(@PathVariable long id) {
        User user = service.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[%s] not found", id)));

        // HATEOAS
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    // @Valid 애노테이션을 사용하여 입력받은 User 데이터에 대해 유효성 체크를 실행한다.
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();    // 201 CREATED
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        service.deleteById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[%s] not found", id)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> putUser(@PathVariable long id, @RequestBody User user) {
        service.updateById(id, user)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[%s] not found", id)));

        return ResponseEntity.noContent().build();  // 204 No Content
    }

}
