/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.initalizer;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallFilter;
import com.google.common.base.Strings;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.redis.JedisPlugin;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.dialect.DB2Dialect;
import com.jfinal.plugin.activerecord.dialect.H2Dialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.jfinal.sog.ctxbox.ClassBox;
import com.jfinal.sog.ctxbox.ClassType;
import com.jfinal.sog.ftl.BlockDirective;
import com.jfinal.sog.ftl.ExtendsDirective;
import com.jfinal.sog.ftl.OverrideDirective;
import com.jfinal.sog.ftl.PrettyTimeDirective;
import com.jfinal.sog.ftl.SuperDirective;
import com.jfinal.sog.interceptor.ContextInterceptor;
import com.jfinal.sog.interceptor.SystemLogProcessor;
import com.jfinal.sog.interceptor.autoscan.AutoOnLoadInterceptor;
import com.jfinal.sog.interceptor.syslog.SysLogInterceptor;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;

import static com.jfinal.sog.initalizer.InitConst.*;

/**
 * <p>
 * .
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-12-12 13:50
 * @since JDK 1.5
 */
public class AppConfig extends JFinalConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Override
    public void configConstant(Constants constants) {
        setProperties(ConfigProperties.getConfigProps());
        constants.setDevMode(getPropertyToBoolean(DEV_MODE, false));
        view_path = getProperty(VIEW_PATH, "/WEB-INF/views/");
        if (!StringKit.isBlank(view_path)) {
            setViewPath = true;
            constants.setBaseViewPath(view_path);
        }
        appName = getProperty(APP, "app");
        domain = getProperty(DOMAIN, DEFAULT_DOMAIN);
        String view_type = getProperty(VIEW_TYPE);
        if (!StringKit.isBlank(view_type)) {
            setViewType(constants, view_type);
        } else {
            // default freemarker view.
            setFtlSharedVariable();
        }
        String view_404 = getProperty(VIEW_404);
        if (!Strings.isNullOrEmpty(view_404)) {
            constants.setError401View(view_404);
        }
        String view_500 = getProperty(VIEW_500);
        if (!Strings.isNullOrEmpty(view_500)) {
            constants.setError500View(view_500);
        }
    }


    @Override
    public void configRoute(Routes routes) {
        this.routes = routes;
        routes.add(new AutoBindRoutes());
    }

    @Override
    public void configPlugin(Plugins plugins) {
        String db_url = getProperty(DB_URL);
        final boolean devMode = getPropertyToBoolean(DEV_MODE, false);
        initDataSource(plugins, db_url, devMode);

        if (getPropertyToBoolean(SECURITY, false)) {
            plugins.add(new ShiroPlugin(this.routes));
        }
        if (getPropertyToBoolean(CACHE, false)) {
            plugins.add(new EhCachePlugin());
        }

        if (getPropertyToBoolean(JOB, false)) {
            plugins.add(new QuartzPlugin());
        }

        final String mongo_host = getProperty(MONGO_HOST, MongodbPlugin.DEFAULT_HOST);
        final String mongo_url = getProperty(MONGO_URL, StringUtils.EMPTY);
        if (!Strings.isNullOrEmpty(mongo_host) || !Strings.isNullOrEmpty(mongo_url)) {
            int mongo_port = getPropertyToInt(MONGO_PORT, MongodbPlugin.DEFAUL_PORT);
            String mongo_db = getProperty(MONGO_DB, "test");
            boolean moriph = getPropertyToBoolean(MONGO_MORIPH, false);
            String pkgs = getProperty(MONGO_MORIPH_PKGS, MongodbPlugin.DEFAULT_PKGS);
            final MongodbPlugin mongodb = new MongodbPlugin(mongo_host, mongo_port, mongo_db, moriph, pkgs);
            plugins.add(mongodb);
        }

        final String redis_host = getProperty(REDIS_HOST, StringUtils.EMPTY);
        if (!Strings.isNullOrEmpty(redis_host)) {
            int port = getPropertyToInt(REDIS_PORT, JedisPlugin.DEFAULT_PORT);
            final JedisPlugin jedis = new JedisPlugin(redis_host, port, 2000);
            plugins.add(jedis);
        }

    }

    /**
     * init databases.
     *
     * @param plugins plugin.
     * @param db_url  db_url
     * @param devMode devmode
     */
    private void initDataSource(Plugins plugins, String db_url, boolean devMode) {
        if (!Strings.isNullOrEmpty(db_url)) {
            // 如果配置了数据库地址，则启用数据库插件
            final DruidPlugin druidPlugin = new DruidPlugin(
                    db_url
                    , getProperty(DB_USERNAME)
                    , getProperty(DB_PASSWORD));
            // 增加监控统计和防SQL注入拦截
            druidPlugin.setFilters("stat,wall");
            final WallFilter wall = new WallFilter();
            wall.setDbType(JdbcConstants.MYSQL);
            druidPlugin.addFilter(wall);
            plugins.add(druidPlugin);

            //  setting db table name like 'dev_info'
            final AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin, SimpleNameStyles.LOWER_UNDERLINE);

            // 根据db_url判断是什么数据库,使用druid的方法判断
            String dbtype = JdbcUtils.getDbType(db_url, StringUtils.EMPTY);
            if (!StringUtils.equals(dbtype, JdbcConstants.MYSQL)) {
                if (StringUtils.equals(dbtype, JdbcConstants.ORACLE)) {
                    atbp.setDialect(new OracleDialect());
                } else if (StringUtils.equals(dbtype, JdbcConstants.POSTGRESQL)) {
                    atbp.setDialect(new PostgreSqlDialect());
                } else if (StringUtils.equals(dbtype, JdbcConstants.DB2)) {
                    atbp.setDialect(new DB2Dialect());
                } else if (StringUtils.equals(dbtype, JdbcConstants.H2)) {
                    atbp.setDialect(new H2Dialect());
                } else if (StringUtils.equals(dbtype, "sqlite")) {
                    atbp.setDialect(new Sqlite3Dialect());
                } else {
                    System.err.println("database type is use mysql.");
                }
            }
            atbp.setShowSql(devMode);
            plugins.add(atbp);
            if (getPropertyToBoolean(DB_SQLINXML, false)) {
                plugins.add(new SqlInXmlPlugin());
            }
        }

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        try {
            URL config_url = com.google.common.io.Resources.getResource("syslog.json");
            if (config_url != null) {
                SysLogInterceptor sysLogInterceptor = new SysLogInterceptor();
                sysLogInterceptor = sysLogInterceptor.setLogProcesser(new SystemLogProcessor(), config_url.getPath());
                if (sysLogInterceptor != null) {
                    interceptors.add(sysLogInterceptor);
                }
            }
        } catch (IllegalArgumentException ignored) {
            // ingored.
        }

        interceptors.add(new ContextInterceptor());

        new AutoOnLoadInterceptor(interceptors).load();
    }

    @Override
    public void configHandler(Handlers handlers) {
//        handlers.add(new SessionHandler());
        //访问路径是/admin/monitor
        DruidStatViewHandler dvh = new DruidStatViewHandler("/admin/monitor", new IDruidStatViewAuth() {
            public boolean isPermitted(HttpServletRequest request) {//获得查看权限
//				HttpSession hs = request.getSession(false);
//				return (hs != null && hs.getAttribute("$admin$") != null);
                return true;
            }
        });
        handlers.add(dvh);
    }

    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        List<Class> appCliasses = ClassBox.getInstance().getClasses(ClassType.APP);
        if (appCliasses != null && !appCliasses.isEmpty()) {
            for (Class appCliass : appCliasses) {

                AppLoadEvent event;
                try {
                    event = (AppLoadEvent) appCliass.newInstance();
                    event.load();
                } catch (Throwable t) {
                    logger.error("load event is error!", t);
                    // ingore
                }
            }
        }

        ClassBox.getInstance().clearBox();
    }

    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
    }


    /**
     * set view type.
     *
     * @param constants jfinal constant.
     * @param view_type view type.
     */
    private void setViewType(Constants constants, String view_type) {
        final ViewType viewType = ViewType.valueOf(view_type.toUpperCase());
        if (viewType == ViewType.FREE_MARKER) {
            constants.setFreeMarkerViewExtension(".ftl");
            setFtlSharedVariable();
        }
        constants.setViewType(viewType);
    }

    /**
     * set freemarker variable.
     */
    private void setFtlSharedVariable() {
        // custmer variable
        final Configuration config = FreeMarkerRender.getConfiguration();
        config.setSharedVariable("block", new BlockDirective());
        config.setSharedVariable("extends", new ExtendsDirective());
        config.setSharedVariable("override", new OverrideDirective());
        config.setSharedVariable("super", new SuperDirective());
        // 增加日期美化指令（类似 几分钟前）
        config.setSharedVariable("prettyTime", new PrettyTimeDirective());
    }


    /**
     * Global routing system configuration.
     */
    private Routes routes;

    private static final String DEFAULT_DOMAIN = "http://127.0.0.1:8080/app";

    private static String view_path;

    private static String  domain;
    private static boolean setViewPath;
    private static String  appName;

    public static String getAppName() {
        return appName;
    }

    public static String getBaseViewPath() {
        return view_path;
    }

    public static String getDomain() {
        return domain;
    }

    public static boolean isSetViewPath() {
        return setViewPath;
    }
}
