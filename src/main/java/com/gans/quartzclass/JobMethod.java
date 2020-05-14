package com.gans.quartzclass;

import com.gans.entity.Myquartz;
import com.gans.service.QuartzService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;


/**
 * 提供Job任务相关的方法
 * @author xiaohe
 */
@Component
public class JobMethod
{
    @Resource( name = "schedulerFactoryBean" )
    private Scheduler scheduler;

    @Autowired
    private QuartzService quartzService;

    private static Log log = LogFactory.getLog( JobMethod.class );


    /**
     * 任务框架初始化方法
     * @throws
     */
    @PostConstruct
    public void init()
    {
        /* 从数据库获得所有的任务信息记录 */
        List<Myquartz> jobList = quartzService.selectAllQuartz();

        if ( jobList != null && !jobList.isEmpty() )
        {
            for ( Myquartz scheduleJob : jobList )
            {
                /* 判断任务状态，是否为执行状态 */

                TriggerKey triggerKey = TriggerKey.triggerKey( scheduleJob
                        .getJobName(), scheduleJob.getJobGroup() );
                CronTrigger trigger;
                try
                {
                    trigger = (CronTrigger) scheduler.getTrigger( triggerKey );
                    if ( null == trigger )
                    {
                        JobBuilder builder = JobBuilder.newJob().withIdentity(
                                scheduleJob.getJobName(),
                                scheduleJob.getJobGroup() );
                        builder.ofType((Class<? extends Job>) Class.forName(scheduleJob.getBeanClass()));
                        builder.withDescription(scheduleJob.getDescription());
                        JobDetail jobDetail  = builder.build();
                        jobDetail.getJobDataMap().put( "scheduleJob",
                                scheduleJob );

                        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                                .cronSchedule( scheduleJob.getCronExpression() );

                        trigger = TriggerBuilder.newTrigger().withIdentity(
                                scheduleJob.getJobName(),
                                scheduleJob.getJobGroup() ).withSchedule(
                                scheduleBuilder ).build();
                        scheduler.scheduleJob( jobDetail, trigger );
                    }else {
                        /* Trigger已存在，那么更新相应的定时设置 */
                        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                                .cronSchedule( scheduleJob.getCronExpression() );

                        /* 按新的cronExpression表达式重新构建trigger */
                        trigger = trigger.getTriggerBuilder().withIdentity(
                                triggerKey ).withSchedule( scheduleBuilder )
                                .build();

                        /* 按新的trigger重新设置job执行 */
                        scheduler.rescheduleJob( triggerKey, trigger );
                    }
                }
                catch (SchedulerException | ClassNotFoundException e )
                {
                    log.error( "Task init failed.", e );
                }
            }
        }
    }


    /**
     * 暂停一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob( Myquartz scheduleJob )
    {
        JobKey jobKey = JobKey.jobKey( scheduleJob.getJobName(), scheduleJob.getJobGroup() );
        try
        {
            scheduler.pauseJob( jobKey );
        }
        catch ( SchedulerException e )
        {
            log.error( "Task pause failed.", e );
        }
    }


    /**
     * 恢复一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob( Myquartz scheduleJob )
    {
        JobKey jobKey = JobKey.jobKey( scheduleJob.getJobName(), scheduleJob.getJobGroup() );
        try
        {
            scheduler.resumeJob( jobKey );
        }
        catch ( SchedulerException e )
        {
            log.error( "Task resume failed.", e );
        }
    }


    /**
     * 删除一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void deleteJob( Myquartz scheduleJob )
    {
        JobKey jobKey = JobKey.jobKey( scheduleJob.getJobName(), scheduleJob.getJobGroup() );
        try
        {
            scheduler.deleteJob( jobKey );
        }
        catch ( SchedulerException e )
        {
            log.error( "Task delete failed.", e );
        }
    }


    /**
     * 立即执行job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void runJobNow( Myquartz scheduleJob )
    {
        JobKey jobKey = JobKey.jobKey( scheduleJob.getJobName(), scheduleJob.getJobGroup() );
        try
        {
            scheduler.triggerJob( jobKey );
        }
        catch ( SchedulerException e )
        {
            log.error( "Task run failed.", e );
        }
    }


    /**
     * 更新job时间表达式
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron( Myquartz scheduleJob ) throws SchedulerException
    {
        TriggerKey triggerKey = TriggerKey.triggerKey( scheduleJob.getJobName(),
                scheduleJob.getJobGroup() );
        /* 获取trigger，即在spring配置文件中定义的 bean id="schedulerFactoryBean" */
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger( triggerKey );
        /* 表达式调度构建器 */
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule( scheduleJob
                .getCronExpression() );
        /*按新的cronExpression表达式重新构建trigger */
        trigger = trigger.getTriggerBuilder().withIdentity( triggerKey )
                .withSchedule( scheduleBuilder ).build();
        /*按新的trigger重新设置job执行 */
        scheduler.rescheduleJob( triggerKey, trigger );
    }


    /**
     * 判断表达式是否可用
     * @param cron
     * @return
     * @throws
     */
    public boolean checkCron( String cron )
    {
        try
        {
            CronScheduleBuilder.cronSchedule( cron );
        }
        catch ( Exception e )
        {
            return(false);
        }
        return(true);
    }
}
