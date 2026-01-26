package com.rag.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rag.auth.dao.RoleMapper;
import com.rag.auth.dao.UserMapper;
import com.rag.auth.entity.Role;
import com.rag.auth.entity.User;
import com.rag.auth.service.AuthService;
import com.rag.common.constant.AuthConstants;
import com.rag.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        return authService.login(request);
    }


    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader(AuthConstants.AUTH_HEADER) String header) {
       return authService.logout(header);
    }
}