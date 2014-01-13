/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.modules;

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
    public static Res dao = new Res();
}
