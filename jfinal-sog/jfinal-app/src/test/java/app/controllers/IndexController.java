/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.controllers;

import app.module.Task;
import com.github.sog.db.filter.Condition;
import com.github.sog.db.filter.Filter;
import com.github.sog.controller.BasicController;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-24 11:00
 * @since JDK 1.6
 */
public class IndexController extends BasicController {

    public void index() {
        Task.dao.findById(1);
        renderJson("abc");
    }



    public void filter(){
        Filter<Task> filter = new Filter<Task>(Task.dao);
        filter.addCondition("title", Condition.Operator.eq, "Try SpringFuse");
        filter.addCondition("", Condition.Operator.like, "ccc");
        filter.addCondition("", Condition.Operator.like, "ccc");
        filter.addCondition("", Condition.Operator.like, "ccc");
        filter.addCondition("", Condition.Operator.like, "ccc");
        filter.addCondition("", Condition.Operator.like, "ccc");
        renderJson(filter.fetch("id, title"));
    }
}
