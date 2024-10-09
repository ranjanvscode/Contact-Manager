package com.scm.service;
import com.scm.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User  saveUser(User user);

    Optional<User> getUserById(String id);
    
    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(User user);

    boolean isUserExistById(String id);
    
    boolean isUserExistByEmail(String email);

    Optional<User> getUserByEmailAndPassword(String email,String password);
    
    User getUserByEmail(String email);
}
