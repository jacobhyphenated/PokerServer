package com.hyphenated.card.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by Nitin on 28-10-2015.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final String SCHEDULER_NAME = "POKER_QUARTZ_SCHEDULER";
    public static final String JOB_GROUP = "QUARTZ_GAME_JOB_GROUP";
    public static final String JOB_NAME_PREFIX = "createJobDetailFactoryBeanFor";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PlatformTransactionManager transactionManager;

//    @Bean
//    public SchedulerFactoryBean createSchedulerFactoryBean() {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//
//        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        schedulerFactoryBean.setJobFactory(jobFactory);
//
//        schedulerFactoryBean.setTransactionManager(transactionManager);
//        schedulerFactoryBean.setOverwriteExistingJobs(true);
//        schedulerFactoryBean.setSchedulerName(SCHEDULER_NAME);
//        return schedulerFactoryBean;
//    }
}
