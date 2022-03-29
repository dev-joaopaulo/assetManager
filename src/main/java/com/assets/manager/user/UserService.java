package com.assets.manager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getUsers(){
        List<UserDTO> list = userRepository.findAll().stream().map(UserDTO::create).collect(Collectors.toList());
        return list;
    }

}
