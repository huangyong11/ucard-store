package com.ucard.core.database;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


public class DataSourceHelper {

    static Logger log = LoggerFactory.getLogger(DataSourceHelper.class);

    public static DataSource getDataSource(DataSourceConfig config) {
        if (config.isEmbedded) {
            return embeddedDataSource(config);
        } else {
            return mysqlDataSource(config);
        }
    }

    private static DataSource embeddedDataSource(DataSourceConfig config) {
        log.debug("create embeddedDatabase");
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setType(config.getEmbeddedType());

        if (config.getEmbeddedType() == EmbeddedDatabaseType.HSQL) {
            builder.addScript("classpath:com/hylax/plat/webframework/database/hsql_init.sql");
        } else if (config.getEmbeddedType() == EmbeddedDatabaseType.H2) {
            builder.addScript("classpath:com/hylax/plat/webframework/database/h2_init.sql");
        }

        for (String script : config.getHsqldbScriptList()) {
            builder.addScript("classpath:" + script);
        }

        EmbeddedDatabase embeddedDatabase = builder.build();
        return embeddedDatabase;
    }

    private static DataSource mysqlDataSource(DataSourceConfig config) {

        PoolProperties p = new PoolProperties();
        p.setUrl(config.databaseUrl);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(config.databaseUsername);
        p.setPassword(config.databasePassword);
        p.setJmxEnabled(true);
        if (config.getUseUtcTimeZone()) {
            p.setInitSQL("SET time_zone = '+0:00'");
        }
        // when database broke, reconnect
        p.setTestWhileIdle(true);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setValidationInterval(30000);

        p.setMaxActive(100);
        // idle connections survive 60s by default
        p.setMaxIdle(10);
        // if idle for long time, we don't keep them
        p.setMinIdle(0);
        p.setMaxWait(10000);
        // If connection is broken
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setRemoveAbandonedTimeout(60);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setPoolProperties(p);
        return dataSource;
    }
}
