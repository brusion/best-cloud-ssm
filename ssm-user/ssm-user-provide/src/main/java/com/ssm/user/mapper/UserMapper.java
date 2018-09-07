package com.ssm.user.mapper;

import com.ssm.user.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int delete(Integer userId);

    int insert(User user);

    List<User> getList();

    User getById(Integer userId);

    int update(User user);
}