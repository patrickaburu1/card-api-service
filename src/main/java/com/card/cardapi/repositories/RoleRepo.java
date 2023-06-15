package com.card.cardapi.repositories;


import com.card.cardapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
