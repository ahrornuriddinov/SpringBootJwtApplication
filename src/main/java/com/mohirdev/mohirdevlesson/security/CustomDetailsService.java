package com.mohirdev.mohirdevlesson.security;

import com.mohirdev.mohirdevlesson.entity.Authority;
import com.mohirdev.mohirdevlesson.exception.UserNotActivatedException;
import com.mohirdev.mohirdevlesson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String lowerCaseName = username.toLowerCase();

        return userRepository
                .findByUsername(lowerCaseName)
                .map(user -> createSpringSecurityUser(lowerCaseName,user))
                .orElseThrow(()->new UserNotActivatedException("User " + username + " is not found in the database"));
    }

    private User createSpringSecurityUser(String username, com.mohirdev.mohirdevlesson.entity.User user){
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + username + " is not activated");
        }
            List<GrantedAuthority> grantedAuthorities = user
                    .getAuthorities()
                    .stream()
                    .map(Authority::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new User(username,user.getPassword(),grantedAuthorities);

    }
}
