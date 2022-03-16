package com.assets.manager.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String hello(){
        return "This is the rest api webservice for assets.manager";
    }

    @GetMapping("/user")
    public UserDetails userInfo(@AuthenticationPrincipal UserDetails user){
        return user;
    }


}
