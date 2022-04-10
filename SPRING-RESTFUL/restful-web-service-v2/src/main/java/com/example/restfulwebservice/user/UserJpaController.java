package com.example.restfulwebservice.user;

import com.example.restfulwebservice.post.Post;
import com.example.restfulwebservice.post.PostRepository;
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
@RequestMapping("/jpa")
public class UserJpaController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> retrieveUser(@PathVariable long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() ->
//                        new UserNotFoundException(String.format("ID[%s] not found", id)));

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("ID[%s] not found", id)));

        // HATEOAS
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("ID[%s] not found", id)))
                .getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<User> createPost(@PathVariable long id,
                                           @Valid @RequestBody Post post) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("ID[%s] not found", id)));
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
