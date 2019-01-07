package com.xu.seckill.mapper;

import com.xu.seckill.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface UserMapper {

    @Select("select * from ms_user where phone = #{phone}")
    User getByPhone(@Param("id") String phone);

    @Update("update ms_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);
}
