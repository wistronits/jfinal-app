/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.plugin.quartz;

import com.jfinal.ext.kit.Reflect;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import static com.google.common.base.Throwables.propagate;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class QuartzPlugin implements IPlugin {
    private static final String JOB = "job";

    private static final Logger logger = Logger.getLogger(QuartzPlugin.class);

    private final Scheduler sched;
    private final Properties properties;

    public QuartzPlugin(Properties properties) {
        this.properties = properties;
        Scheduler tmp_sched = null;
        try {
            tmp_sched = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            propagate(e);
        }
        this.sched = tmp_sched;
    }


    @Override
    public boolean start() {

        if (logger.isDebugEnabled()) {
            logger.debug("------------load Propteries---------------");
            logger.debug(properties.toString());
            logger.debug("------------------------------------------");
        }
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + StringUtils.EMPTY;
            // 如果以 job 或者不是job以及启动的信息，则不进行处理
            if (StringUtils.equals(JOB, key) || !key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = properties.get(key) + StringUtils.EMPTY;
            String jobCronExp = properties.getProperty(cronKey(key)) + StringUtils.EMPTY;
            Class<Job> clazz = Reflect.on(jobClassName).get();
            JobDetail job = newJob(clazz)
                    .withIdentity(jobClassName)
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity(jobClassName)
                    .withSchedule(cronSchedule(jobCronExp))
                    .startNow()
                    .build();

            Date ft = null;
            try {
                ft = sched.scheduleJob(job, trigger);
                sched.start();
            } catch (SchedulerException e) {
                propagate(e);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                        + jobCronExp);
            }
        }
        return true;
    }

    private String enable(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "enable";
    }

    private String cronKey(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "cron";
    }

    private boolean isEnableJob(String enableKey) {
        Object enable = properties.get(enableKey);
        return !(enable != null && "false".equalsIgnoreCase((enable + "").trim()));
    }


    @Override
    public boolean stop() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            propagate(e);
        }
        return true;
    }

}
