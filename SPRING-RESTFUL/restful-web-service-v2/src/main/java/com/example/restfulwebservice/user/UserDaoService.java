package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static long usersCount = 3;

    static {
        users.add(new User(1L, "pass1", "123456-7890123", "Kenneth", new Date()));
        users.add(new User(2L, "pass2", "123456-7890123", "Alice", new Date()));
        users.add(new User(3L, "pass3", "123456-7890123", "Elena", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public Optional<User> findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> deleteById(long id) {
        Optional<User> user = findById(id);
        user.ifPresent(x -> users.remove(x));
        return user;
    }

    public Optional<User> updateById(long id, User user) {
        for (User storedUser : users) {
            if (storedUser.getId() == id) {
                storedUser.setName(user.getName());
                storedUser.setJoinDate(user.getJoinDate());
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
