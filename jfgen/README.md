# JFinal-GEN

## Usage

set JFGEN_HOEM with into you path.

example, jfgen unzip to `~/Workthing/02_sogyf/jfinal-gap/jfgen`:
    
    export PATH=/bin:/sbin:/usr/local/bin:/usr/bin:/usr/sbin:/usr/local/sbin:$MYSQL_HOME/bin:/opt/X11/bin:$NPM_APP/bin:$PYTHON_APP:$HOME/.rvm/bin:$RUBY_APP/bin:$TEXLIVE_HOME/bin/universal-darwin:$ANDROID_HOME/tools:~/Workthing/02_sogyf/jfinal-gap/jfgen:


then run

	~
	~ JFinal App gen.
	~ Usage: jfgen cmd application_name [-options]
	~
	~ with,  new      Create a new application
	~        war      Export the application as a standalone WAR archive
	~        idea     Convert the project to Intellij IDEA project
	~        help     Show jfgen help
	~

## Step by step to careate a applicaton.
> In this example. we will create a name for `towork` applications and use Maven or converted into common development of IDEA projct.

1. First
	
		$ cd ~/projects
		$ jfgen new towork
		$ tree towork
		towork
		├── pom.xml
		└── src
    		├── main
    		│   ├── java
    		│   │   └── app
    		│   │       ├── controllers
    		│   │       │   └── IndexController.java
    		│   │       ├── dtos
    		│   │       ├── interceptors
    		│   │       ├── jobs
    		│   │       ├── models
    		│   │       └── validators
    		│   ├── resources
    		│   │   ├── application.conf
    		│   │   ├── ehcache.xml
    		│   │   ├── logback.xml
    		│   │   └── sqlcnf
    		│   └── webapp
    		│       ├── WEB-INF
    		│       │   └── views
    		│       │       └── index.ftl
    		│       └── static
    		└── test
        		├── java
        		│   └── app
        		└── resources

		20 directories, 6 files

2. You can now use `Intellij IDEA` open `pom.xml` AND import project.
3. Or you can use the `jfgen idea` to generate idea project file.
		
		$ cd towork
		$ jfgen idea
		
4. Next, you can enjoy the use of idea for the developement of.