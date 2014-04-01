/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

import com.github.sog.initalizer.InitConst;
import com.github.sog.kit.io.ResourceKit;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.jfinal.server.ServerFactory;

import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-03-30 9:18
 * @since JDK 1.6
 */
public class JFinalAppServer {

    public static void main(String[] args) {

        final Map<String, String> conf = ResourceKit.readProperties("application.conf");
        int scan_in = 0;
        String scan = conf.get(InitConst.DEV_SCAN);
        if (!Strings.isNullOrEmpty(scan)) {
            scan_in = Ints.tryParse(scan);
        }
        ServerFactory.getServer("src/main/webapp", 9000, "/" + conf.get(InitConst.APP), scan_in).start();

    }
}
