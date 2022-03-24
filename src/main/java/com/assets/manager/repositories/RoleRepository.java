package com.assets.manager.repositories;

import com.assets.manager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName (String name);

}
