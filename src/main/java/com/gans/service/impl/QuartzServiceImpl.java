package com.gans.service.impl;

import com.gans.dao.MyquartzMapper;
import com.gans.entity.Myquartz;
import com.gans.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    MyquartzMapper mapper;

    @Override
    public List<Myquartz> selectAllQuartz() {
        return mapper.selectAll();
    }

    @Override
    public Myquartz seleceQuartz(long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertQuartz(Myquartz myquartz) {
        return mapper.insertSelective(myquartz);
    }

    @Override
    public int deleteQuartz(long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateQuartz(Myquartz myquartz) {
        return mapper.updateByPrimaryKeySelective(myquartz);
    }
}
