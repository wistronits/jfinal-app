/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.plugin.redis;

import com.jfinal.log.Logger;

import java.io.Serializable;

public class QueueProducer {
    protected final Logger logger = Logger.getLogger(getClass());
    String queueName;

    private QueueProducer(String queueName) {
        this.queueName = queueName;
    }

    public static QueueProducer create(String queueName) {
        return new QueueProducer(queueName);
    }

    public boolean publish(Serializable message) {
        return JedisKit.lpush("queue-" + queueName, message) >= 1 ? true : false;
    }
}
