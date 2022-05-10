package com.ljb.springbootseta.mapper;

import com.ljb.springbootseta.entity.Ue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Daoa {

    @Select(value = { "SELECT * FROM `user` WHERE name=#{name}" })
    public Ue denglu(@Param(value = "name") String name);


    @Select(value = { "SELECT password FROM `user` WHERE name=#{name}" })
    public String dl(@Param(value = "name") String name);
}
