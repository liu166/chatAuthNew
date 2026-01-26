package com.rag.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rag.auth.dao.RoleMapper;
import com.rag.auth.dao.UserMapper;
import com.rag.auth.dto.AuthDTO;
import com.rag.auth.entity.Role;
import com.rag.auth.entity.User;
import com.rag.auth.service.AuthService;
import com.rag.auth.vo.Result;
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
    public Result login(@RequestBody AuthDTO dto) {
        return authService.login(dto);
    }


    @PostMapping("/logout")
    public Result logout(@RequestHeader(AuthConstants.AUTH_HEADER) String header) {
       return authService.logout(header);
    }

    // 新增注册接口
    @PostMapping("/register")
    public Result register(@RequestBody AuthDTO dto) {
        return authService.register(dto);
    }
}