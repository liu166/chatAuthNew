package com.rag.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rag.auth.dao.RoleMapper;
import com.rag.auth.dao.UserMapper;
import com.rag.auth.dto.AuthDTO;
import com.rag.auth.entity.User;
import com.rag.auth.vo.Result;
import com.rag.common.constant.AuthConstants;
import com.rag.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Result login(AuthDTO dto) {

        String userName = dto.getUserName();
        String passWord = dto.getPassWord();

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        if (null == user || !passwordEncoder.matches(passWord, user.getPassword())) {
            throw new RuntimeException("账户或密码错误");
        }
        user.setRoles(roleMapper.getRolesByUserId(user.getId()));


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


       return Result.ok(token);
    }

    public Result logout(String header) {
        String token = header.replace(AuthConstants.TOKEN_PREFIX, "");


// ⚡ 使用注入的 jwtUtil.parseToken
        String sessionId = jwtUtil.parseToken(token).get("sessionId", String.class);


        redisTemplate.delete(AuthConstants.REDIS_SESSION_PREFIX + sessionId);
        return Result.ok(token);
    }

    public Result register(AuthDTO dto) {

        String userName = dto.getUserName();
        String passWord = dto.getPassWord();


        if (userName == null || passWord == null) {
            throw new RuntimeException("用户名或密码不能为空");
        }


// 1️⃣ 检查用户是否已存在
        User existing = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }


// 2️⃣ 密码加密
        String encodedPassword = passwordEncoder.encode(passWord);


// 3️⃣ 构造用户实体
        User user = new User();
        user.setUsername(userName);
        user.setPassword(encodedPassword);
        userMapper.insert(user);


// 4️⃣ 可以直接返回注册成功提示
        return Result.ok();
    }
}
