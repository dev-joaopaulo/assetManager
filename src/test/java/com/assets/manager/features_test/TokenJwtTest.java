package com.assets.manager.features_test;

import com.assets.manager.ManagerApplication;
import com.assets.manager.config.security.jwt.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class)
public class TokenJwtTest {
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Test
    public void testToken() {

        // Le usuário
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        assertNotNull(user);

        // Gera token
        String jwtToken = JwtUtil.createToken(user);
        System.out.println(jwtToken);
        assertNotNull(jwtToken);

        // Valida Token
        boolean ok = JwtUtil.isTokenValid(jwtToken);
        assertTrue(ok);

        // Valida login
        String login = JwtUtil.getLogin(jwtToken);
        assertEquals("admin",login);

        // Valida roles
        List<GrantedAuthority> roles = JwtUtil.getRoles(jwtToken);
        assertNotNull(roles);
        System.out.println(roles);

        boolean isRoleAdmin = false;

        for(GrantedAuthority authority : roles){
            if(Objects.equals(authority.getAuthority(), "ROLE_ADMIN")){
                isRoleAdmin = true;
            }
        }

        assertTrue(isRoleAdmin);
    }

}