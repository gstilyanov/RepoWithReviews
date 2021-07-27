package com.stilyanov.repository.service.contract;

import com.stilyanov.repository.model.User;

public interface UserService {

    void create(User user);

    User getByUsername(String username);
}
