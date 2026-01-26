package com.rag.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rag.auth.dao.RoleMapper;
import com.rag.auth.dao.UserMapper;
import com.rag.auth.entity.User;
import com.rag.common.constant.AuthConstants;
import com.rag.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> login(Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");


        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));


        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("账户或密码错误");
        }


// 生成 sessionId
        String sessionId = UUID.randomUUID().toString();


// ⚡ 使用注入的 jwtUtil.createToken
        String token = jwtUtil.createToken(sessionId);


// 存 Redis
        Map<String, Object> session = Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "roles", user.getRoles().stream()
                        .map(r -> r.getName())
                        .collect(Collectors.toList())
        );


        redisTemplate.opsForValue().set(
                AuthConstants.REDIS_SESSION_PREFIX + sessionId,
                session,
                Duration.ofHours(2)
        );


        return Map.of("token", token);
    }

    public Map<String, Object> logout(String header) {
        String token = header.replace(AuthConstants.TOKEN_PREFIX, "");


// ⚡ 使用注入的 jwtUtil.parseToken
        String sessionId = jwtUtil.parseToken(token).get("sessionId", String.class);


        redisTemplate.delete(AuthConstants.REDIS_SESSION_PREFIX + sessionId);
        return Map.of("message", "logout success");
    }
}
