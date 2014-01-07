#jfinal-app

> 感谢：[@JFinal](http://git.oschina.net/jfinal/jfinal) [@JFinal-ext](http://git.oschina.net/zhouleib1412/jfinal-ext)

1. 在 `JFinal`基础上，参考`Play 1.2`配置方式，将大部分的配置整合到一个配置文件，并支持动态启动相关插件等！
2. 需要使用`JDK-1.6` 以及 `Servlet 3.0` 以上版本
3. 通过`javax.servlet.ServletContainerInitializer`(需要`Servlet3.0`以上容器)的方式去掉了`web.xml`的配置

## 一、Maven 使用说明

由于中心仓库中对`Ehcache@2.6.7`版本不存在，需要使用`OSC`的Maven仓库来替换掉中心仓库，步骤如下：

1. 创建个人目录文件夹下的 `.m2/settings.xml`（如果已有，则忽略）
> 如果这个目录不存在或者这个`settings.xml`文件不存在的话，手动创建`.m2`目录，然后在`Maven`工具的`conf`包下找到`settings.xml`配置文件，比如我的在 ` /usr/local/Cellar/maven/3.1.1/libexec/conf/settings.xml`， 这个目录为 ` /usr/local/Cellar/maven/3.1.1/libexec/`为 `Maven_HOME`,将这个文件拷贝到 `~/.m2/settings.xml`中
2. 修改`settings.xml`配置

	在xml配置文件中的`mirros`节点，修改为如下内容：
	
		<mirrors>
			<mirror>
				<id>nexus-osc</id>
				<mirrorOf>central</mirrorOf> # 替换中心仓库
				<name>Nexus osc</name>
				<url>http://maven.oschina.net/content/groups/public/</url>
			</mirror>
		</mirrors>
	
3. 重新加载工程POM
	
## 二、系统相关配置和主要插件的使用说明

1. `application.cnf`配置说明

	全部配置说明：
	
		app=appname						# 项目名称
		# Whether the develoer model.
		dev.mode=true					# 是否启动开发模式，true启动，false不启动
		#dev.mode=false
		view.type=jsp					# 视图渲染类型，默认为Freemarker，这里可以设置为JSP
		#view.path=
		domain=http://127.0.0.1:8080/horae	# 系统访问路径，因为有些部署需要通过后端渲染模板，而后端模板无法得到当前应用的访问路径，所以需要配置
		# Enable caching.
		cache=true		# 是否启动缓存（EHcache），true启动，false不启用
		# Enable authentication.
		security=false	# 是否启用身份验证框架（Shiro），true启动，false不启用
		# Start the database plug-in configuration db.url said.
		db.url=jdbc:mysql://127.0.0.1:3306/horce?useUnicode=true&characterEncoding=utf-8	# 数据库访问地址
		db.username=horce # 数据库访问用户名
		db.password=horce@loday	# 数据库访问密码
		# true to start the sql way configuration to an XML file.
		db.sqlinxml=true 	# 是否启用 *-sql.xml 配置文件来管理sql， true启动，false不启用
		# Whether to display sql. 
		db.showsql=true   	# 是否在运行过程中显示执行的SQL，true显示，false不显示
		# Whether the task is enabled.
		#job=true  			# 是否启动定时任务（Quartz），true启用，false不启用
		# job config
		#receive.job=app.jobs.BoxCardJob	# 任务1配置，实现类
		#receive.cron=* * * * * ?			# 任务1的cron运行周期配置
		#receive.enable=true				# 任务1是否启用
		# the view basic path default /WEB-INF/views/
		#view.path=/WEB-INF/views/			# 视图地址，默认为 /WEB-INF/views/

		# Enable MongdoDB plugin
		#mongo.host=192.168.1.210			# 如果配置了该项，则表示启动MongDB插件，同时表示MongoDB的地址
		#mongo.port=27017					# MongoDB的端口
		#mongo.db=db							# MongoDB的数据库
		# Enable MongoDB ORM plugin.
		#mongo.morphia=						# 是否启用Mongodb的Morphia的ORM框架

		# Enable Redis Plugin.
		#redis.url=							# 如果配置该项，则表示启用Redis插件（Jredis），同时表示链接Redis的地址

2. `*-sql.xml` SQL管理说明3. 

	如果`application.conf`配置了`db.sqlinxml`且启动了数据库`db.url`的话，那么表示启用了XML管理SQL的功能。
	
	约定文件名必须为`-sql.xml`结尾的配置文件。如上面的`topic-sql.xml`.
	
	内容示例：
			
			<sqlGroup name="topic">
   				 <sql id="categorys">
        			select id, name, description
        			from rt_category
    			</sql>
    			<sql id="updateOnline">
        			UPDATE rm_member SET online=?,this_login_time=?,this_login_ip=? WHERE id=?
    			</sql>
			</sqlGroup>
	
	在代码中获取 `UPDATE rm_member SET online=?,this_login_time=?,this_login_ip=? WHERE id=?`可通过如下代码：
	
			SqlKit.sql("topic.updateOnline")
	
	约定为：`sqlGroup@name.sql@id`
	
3. `interceptors` 使用说明

	1. 全局拦截器
		
		参考示例： `LoginMemberInceptor.java`
		
		1. 新建Java类，
		2. 实现`com.jfinal.aop.Interceptor`
		2. 在类增加注解`com.jfinal.ext.interceptor.autoscan.Interceptor`
		
		框架会自动扫描注解为`com.jfinal.ext.interceptor.autoscan.Interceptor`的拦截器为全局拦截器.
		
	2. 单个拦截器的使用
		
		参考示例：`TopicConditionInceptor.java`、`TopicController.java`
		
		1. 新建Java类,比如`TopicConditionInceptor.java`，
		2. 实现`com.jfinal.aop.Interceptor`
		3. 在需要拦截的方法或者类上增加 `@Before({TopicConditionInceptor.class})`

4. `validators`使用说明
