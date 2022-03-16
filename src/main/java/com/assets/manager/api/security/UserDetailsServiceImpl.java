package com.assets.manager.api.security;

import com.assets.manager.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.assets.manager.domain.User user = userRepository.findByLogin(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        else{
            return User.withUsername(username).password(user.getPassword()).roles("USER").build();
        }

    }
}
