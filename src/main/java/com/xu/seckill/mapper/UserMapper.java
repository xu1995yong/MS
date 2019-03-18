package com.xu.seckill.mapper;

import com.xu.seckill.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from ms_user where phone = #{phone}")
    User getByPhone(@Param("phone") String phone);

    @Select("select * from ms_user where id = #{id}")
    User getById(@Param("id") long id);

    @Update("update ms_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);
}
