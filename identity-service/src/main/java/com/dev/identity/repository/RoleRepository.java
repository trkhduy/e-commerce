package com.dev.identity.repository;

import com.dev.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);
}
