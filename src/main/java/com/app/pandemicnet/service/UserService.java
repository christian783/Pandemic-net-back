package com.app.pandemicnet.service;

import com.app.pandemicnet.dto.UserDTO;
import com.app.pandemicnet.model.Notification;
import com.app.pandemicnet.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User findByPhoneNumber(String phoneNumber);
}
