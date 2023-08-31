package com.mohirdev.mohirdevlesson.controller;

import com.mohirdev.mohirdevlesson.entity.User;
import com.mohirdev.mohirdevlesson.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody User user){
        User save = userService.save(user);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/users")
    public ResponseEntity getall(){
        return ResponseEntity.ok(userService.findAll());
    }
}
