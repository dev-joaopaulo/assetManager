package com.assets.manager.repositories;

import com.assets.manager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName (String login);

}
