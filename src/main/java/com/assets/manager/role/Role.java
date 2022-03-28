package com.assets.manager.role;

import com.assets.manager.user.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<User>();

    @Override
    public String getAuthority() {
        return name;
    }
}
