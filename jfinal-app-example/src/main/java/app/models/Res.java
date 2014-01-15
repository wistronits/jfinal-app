/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.models;

import com.jfinal.plugin.activerecord.Model;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-13 14:48
 * @since JDK 1.6
 */
//@TableBind(tableName = "res")
public class Res extends Model<Res> {
    private static final long serialVersionUID = 6808306115371219964L;
    public static Res dao = new Res();
}
