package org.example.repository;

import org.example.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<String, UserEntity> users = new ConcurrentHashMap<>();

    public UserEntity findByUsername(String username) {
        return users.get(username);
    }

    public void save(UserEntity userEntity) {
        users.put(userEntity.getUsername(), userEntity);
    }

    public List<UserEntity> getAll() {
        return new ArrayList<>(users.values());
    }
}
