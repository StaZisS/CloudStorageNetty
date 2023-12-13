package org.example.service;

import com.google.inject.Inject;
import org.example.entity.UserEntity;
import org.example.exception.UserAlreadyExistsException;
import org.example.repository.UserRepository;


public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    @Inject
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    @Override
    public void register(String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }
        UserEntity newUser = new UserEntity(username, password);
        userRepository.save(newUser);
    }
}
