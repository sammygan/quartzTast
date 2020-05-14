package com.gans.task;

import com.gans.entity.Myquartz;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

public class BaseJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Myquartz myquartz = (Myquartz)jobExecutionContext.getJobDetail().getJobDataMap().get("scheduleJob");
        if(myquartz==null){
            return;
        }
        try {
            Method method = super.getClass().getDeclaredMethod(myquartz.getJobName(), new Class[0]);
            if(method == null){
                return;
            }
            method.invoke(this,new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
