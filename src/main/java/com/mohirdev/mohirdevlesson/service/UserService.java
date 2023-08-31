package com.mohirdev.mohirdevlesson.service;

import com.mohirdev.mohirdevlesson.entity.User;
import com.mohirdev.mohirdevlesson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User save(User user){
        String encode = encoder.encode(user.getPassword());
        user.setPassword(encode);
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
