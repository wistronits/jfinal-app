/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.controllers;

import app.models.Res;
import com.jfinal.config.BasicController;
import com.jfinal.core.ActionKey;
import com.jfinal.sog.render.datatables.core.DatatablesCriterias;
import com.jfinal.plugin.activerecord.Page;
import org.joda.time.DateTime;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-13 14:35
 * @since JDK 1.6
 */
public class IndexController extends BasicController {

    @ActionKey("/")
    public void index() {
        setAttr("now", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        render("/index.ftl");
    }


    public void dt(){
        render("/dt.ftl");
    }

    @ActionKey("/dts")
    public void dts() {
        final DatatablesCriterias dtc = getCriterias();
        final Page<Res> res = Res.dao.paginate(dtc.getDisplayStart(),
                dtc.getDisplaySize(), "select * ", " from res");
        renderDataTables(res, dtc);
    }
}
