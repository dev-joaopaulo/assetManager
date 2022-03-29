package com.assets.manager.user;

import com.assets.manager.config.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping()
    public ResponseEntity get(){
        List<UserDTO> list = service.getUsers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/info")
    public UserDTO userInfo(){
        User user = (User) JwtUtil.getUserDetails();
        return UserDTO.create(user);
    }

}
