package com.card.cardapi.repositories;


import com.card.cardapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,  Long> {

    User findByUsername(String username);

}
