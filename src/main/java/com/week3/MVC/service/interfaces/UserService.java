package com.week3.MVC.service.interfaces;

import com.week3.MVC.entities.User;

public interface UserService {

    boolean isValidUser(User user);
    boolean isUserAlreadyExists(String email);

    User getUserByEmail(String email);

    User save(User user);

    void deleteById(Integer id);
}
