package com.aspire.mapper;

import com.aspire.common.SysMapper;
import com.aspire.pojo.User;

import java.util.List;

/**
 * @author xiaos
 * @create 2018-02-02-15:50
 */
public interface UserMapper extends SysMapper<User> {

    List<User> findAllUserInfo();

    User findByName(String username);
}
