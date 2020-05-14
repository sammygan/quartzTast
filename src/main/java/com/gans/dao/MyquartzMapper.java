package com.gans.dao;

import com.gans.entity.Myquartz;

import java.util.List;

public interface MyquartzMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Myquartz record);

    int insertSelective(Myquartz record);

    Myquartz selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Myquartz record);

    int updateByPrimaryKey(Myquartz record);

    List<Myquartz> selectAll();
}