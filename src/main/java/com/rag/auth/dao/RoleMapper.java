package com.rag.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rag.auth.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    Set<Role> getRolesByUserId(Long userId);
}