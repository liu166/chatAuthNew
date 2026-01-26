package com.rag.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rag.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
