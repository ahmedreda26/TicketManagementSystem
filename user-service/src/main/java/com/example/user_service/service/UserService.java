package com.example.user_service.service;

import com.example.user_service.DTO.RegisterRequest;
import com.example.user_service.model.User;

public interface UserService {
    User registerUser(RegisterRequest request);
}
