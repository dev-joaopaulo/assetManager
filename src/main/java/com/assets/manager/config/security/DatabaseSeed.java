package com.assets.manager.config.security;

import com.assets.manager.models.Role;
import com.assets.manager.repositories.RoleRepository;
import com.assets.manager.models.User;
import com.assets.manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeed {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event){

        seedInitialUsers();
        seedRoles();

    }

    private void seedInitialUsers() {
        //Create admin user
        User admin = User.builder()
                .name("admin")
                .login("admin")
                .email("admin@admin.com")
                .password("$2a$10$dR8EEiTqeeUBKHPmIuz7b.YlBaJcsb1XDY6UsYw7rH26KP/GemAmq")
                .build();

        User user = User.builder()
                .name("user")
                .login("user")
                .email("user@test.com")
                .password(new BCryptPasswordEncoder().encode("user"))
                .build();

        if(userRepository.findByLogin(admin.getLogin()) == null){
            userRepository.save(admin);
        }
        if(userRepository.findByLogin(user.getLogin()) == null){
            userRepository.save(user);
        }
    }

    private void seedRoles(){
        Role roleUser = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        Role roleAdmin = Role.builder()
                .id(2L)
                .name("ROLE_ADMIN")
                .build();

        if(roleRepository.findByName(roleUser.getName()) == null){
            roleRepository.save(roleUser);
        }
        if(roleRepository.findByName(roleAdmin.getName()) == null){
            roleRepository.save(roleAdmin);
        }
    }

}
