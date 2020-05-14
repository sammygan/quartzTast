package com.gans.service.impl;

import com.gans.entity.Myquartz;
import com.gans.service.QuartzService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class QuartzServiceImplTest {
    private ApplicationContext applicationContext;
    private QuartzService quartzService;

    @Before
    public void setUp() throws Exception {
        // 加载spring配置文件
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        quartzService = applicationContext.getBean(QuartzServiceImpl.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        Myquartz myquartz = new Myquartz();
        myquartz.setJobName("addNum");
        myquartz.setDescription("定时插入+1数据");
        myquartz.setCronExpression("0 0,5 * * * ? ");
        myquartz.setBeanClass("com.gans.task.AddNumTask");
        myquartz.setJobStatus("0");
        myquartz.setJobGroup("myTask");
        myquartz.setCreateUser("gans");
        myquartz.setCreateTime(new Date());
        myquartz.setUpdateUser(null);
        myquartz.setUpdateTime(null);
        quartzService.insertQuartz(myquartz);
    }
}