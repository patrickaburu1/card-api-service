package com.card.cardapi.services;



import com.card.cardapi.entities.Role;
import com.card.cardapi.entities.User;

import java.util.List;


public interface UserService {


    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();


}

