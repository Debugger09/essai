package com.ime.api.auth.service;

import com.ime.api.auth.dto.AuthRequest;
import com.ime.api.auth.dto.AuthResponse;
import com.ime.api.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
