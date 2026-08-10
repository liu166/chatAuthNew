package com.rag.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 认证服务安全配置（Gateway 已做鉴权，此处仅保留 CSRF 关闭和路径放行）
 * <p>
 * chatAuthNew 不再做 JWT Filter 拦截，鉴权统一由 Gateway 的 JwtAuthGlobalFilter 处理。
 * 本服务仅负责认证业务：登录、注册、token 验证、登出。
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 全部放行，鉴权由 Gateway 处理
                );
        return http.build();
    }
}
