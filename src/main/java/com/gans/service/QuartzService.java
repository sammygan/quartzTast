package com.gans.service;

import com.gans.entity.Myquartz;

import java.util.List;

public interface QuartzService {
    public List<Myquartz> selectAllQuartz();
    public Myquartz seleceQuartz(long id);
    public int insertQuartz(Myquartz myquartz);
    public int deleteQuartz(long id);
    public int updateQuartz(Myquartz myquartz);

}
