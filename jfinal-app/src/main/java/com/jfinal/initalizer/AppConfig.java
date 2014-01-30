/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.initalizer;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallFilter;
import com.google.common.base.Strings;
import com.jfinal.config.*;
import com.jfinal.ctxbox.ClassBox;
import com.jfinal.ctxbox.ClassType;
import com.jfinal.ext.ftl.*;
import com.jfinal.ext.interceptor.autoscan.AutoOnLoadInterceptor;
import com.jfinal.ext.interceptor.syslog.SysLogInterceptor;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.redis.JedisPlugin;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.initalizer.interceptors.ContextInterceptor;
import com.jfinal.initalizer.interceptors.SqlInXmlInterceptor;
import com.jfinal.initalizer.interceptors.SystemLogProcessor;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;

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

    /**
     * Global routing system configuration.
     */
    private Routes routes;


    /**
     * 默认视图位置
     */
    private static final String BASE_VIEW_PATH = "/WEB-INF/views/";

    private static final String DEFAULT_DOMAIN = "http://127.0.0.1:8080/";

    private static String view_path;

    private static String domain;
    private static boolean setViewPath;

    /**
     * 系统公用位置
     *
     * @return 公用位置
     */
    public static String getBaseViewPath() {
        return view_path;
    }

    public static String getDomain() {
        return domain;
    }

    public static boolean isSetViewPath() {
        return setViewPath;
    }

    @Override
    public void configConstant(Constants constants) {
        setProperties(ConfigProperties.getConfigProps());
        constants.setDevMode(getPropertyToBoolean("dev.mode", false));
        view_path = getProperty("view.path", BASE_VIEW_PATH);
        if (!StringKit.isBlank(view_path)) {
            setViewPath = true;
            constants.setBaseViewPath(view_path);
        }
        domain = getProperty("domain", DEFAULT_DOMAIN);
        String view_type = getProperty("view.type");
        if (!StringKit.isBlank(view_type)) {
            setViewType(constants, view_type);
        }
        String view_404 = getProperty("view.404");
        if (!Strings.isNullOrEmpty(view_404)) {
            constants.setError401View(view_404);
        }
        String view_500 = getProperty("view.500");
        if (!Strings.isNullOrEmpty(view_500)) {
            constants.setError500View(view_500);
        }
    }

    private void setViewType(Constants constants, String view_type) {
        final ViewType viewType = ViewType.valueOf(view_type.toUpperCase());
        if (viewType == ViewType.FREE_MARKER) {
            constants.setFreeMarkerViewExtension(".ftl");
        }
        constants.setViewType(viewType);

        if (viewType == ViewType.FREE_MARKER) {
            // custmer variable
            final Configuration config = FreeMarkerRender.getConfiguration();
            config.setSharedVariable("block", new BlockDirective());
            config.setSharedVariable("extends", new ExtendsDirective());
            config.setSharedVariable("override", new OverrideDirective());
            config.setSharedVariable("super", new SuperDirective());
            // 增加日期美化指令（类似 几分钟前）
            config.setSharedVariable("prettyTime", new PrettyTimeDirective());
        }
    }

    @Override
    public void configRoute(Routes routes) {
        this.routes = routes;
        routes.add(new AutoBindRoutes());
    }

    @Override
    public void configPlugin(Plugins plugins) {
        String db_url = getProperty("db.url");
        final Boolean devMode = getPropertyToBoolean("dev.mode", false);
        if (!Strings.isNullOrEmpty(db_url)) {
            // 如果配置了数据库地址，则启用数据库插件
            final DruidPlugin druidPlugin = new DruidPlugin(
                    db_url
                    , getProperty("db.username")
                    , getProperty("db.password"));
            // 增加监控统计和防SQL注入拦截
            druidPlugin.setFilters("stat,wall");
            final WallFilter wall = new WallFilter();
            wall.setDbType(JdbcConstants.MYSQL);
            druidPlugin.addFilter(wall);
            plugins.add(druidPlugin);

            //  setting db table name like 'dev_info'
            final AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin, SimpleNameStyles.LOWER_UNDERLINE);

            // 根据db_url判断是什么数据库,使用druid的方法判断
            String dbtype = JdbcUtils.getDbType(db_url, "");
            if (!StringUtils.equals(dbtype, JdbcConstants.MYSQL)) {
                if (StringUtils.equals(dbtype, JdbcConstants.ORACLE)) {
                    atbp.setDialect(new OracleDialect());
                } else if (StringUtils.equals(dbtype, JdbcConstants.POSTGRESQL)) {
                    atbp.setDialect(new PostgreSqlDialect());
                } else if (StringUtils.equals(dbtype, "sqlite")) {
                    atbp.setDialect(new Sqlite3Dialect());
                } else {
                    System.err.println("database type is use mysql.");
                }
            }
            atbp.setShowSql(devMode);
            plugins.add(atbp);
            if (getPropertyToBoolean("db.sqlinxml", false)) {
                plugins.add(new SqlInXmlPlugin());
            }
        }

        if (getPropertyToBoolean("security", false)) {
            plugins.add(new ShiroPlugin(this.routes));
        }
        if (getPropertyToBoolean("cache", false)) {
            plugins.add(new EhCachePlugin());
        }

        if (getPropertyToBoolean("job", false)) {
            plugins.add(new QuartzPlugin());
        }

        final String mongo_host = getProperty("mongo.host", MongodbPlugin.DEFAULT_HOST);
        final String mongo_url = getProperty("mongo.url", StringUtils.EMPTY);
        if (!Strings.isNullOrEmpty(mongo_host) || !Strings.isNullOrEmpty(mongo_url)) {
            int mongo_port = getPropertyToInt("mongo.port", MongodbPlugin.DEFAUL_PORT);
            String mongo_db = getProperty("mongo.db", "test");
            boolean moriph = getPropertyToBoolean("mongo.moriph", false);
            final MongodbPlugin mongodb = new MongodbPlugin(mongo_host, mongo_port, mongo_db, moriph);
            plugins.add(mongodb);
        }

        final String redis_host = getProperty("redis.host", StringUtils.EMPTY);
        if (!Strings.isNullOrEmpty(redis_host)) {
            int port = getPropertyToInt("redis.port", JedisPlugin.DEFAULT_PORT);
            final JedisPlugin jedis = new JedisPlugin(redis_host, port, 2000);
            plugins.add(jedis);
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

        if (getPropertyToBoolean("dev.mode", false) && getPropertyToBoolean("db.sqlinxml", false)) {
            interceptors.add(new SqlInXmlInterceptor());
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

        List<Class> appCliasses = ClassBox.getInstance().getClasses(ClassType.APP);
        if (appCliasses != null && !appCliasses.isEmpty()) {
            for (Class appCliass : appCliasses) {

                AppLoadEvent event;
                try {
                    event = (AppLoadEvent) appCliass.newInstance();
                    event.load();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        ClassBox.getInstance().clearBox();
        super.afterJFinalStart();
    }

    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
    }
}
