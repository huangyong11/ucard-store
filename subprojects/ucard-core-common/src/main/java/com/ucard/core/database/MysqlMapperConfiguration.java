package com.ucard.core.database;

import com.ucard.core.database.type.DbStatusTypeHandler;
import com.ucard.core.mapper.VendorMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.scripting.velocity.Driver;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Slf4j
public class MysqlMapperConfiguration {

    @Value("${mysql.url:url}")
    String mysqlUrl;

    @Value("${mysql.username:username}")
    String mysqlUsername;

    @Value("${mysql.password:passport}")
    String mysqlPassword;

    @Value("${mysql.utc.timezone:true}")
    boolean mysqlUseUtcTimezone;

    @Bean
    public JdbcTemplate mysqlJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(mysqlDataSource());
        return jdbcTemplate;
    }

    @Bean
    @Primary
    public DataSource mysqlDataSource() {
        DataSourceConfig config = new DataSourceConfig()
                .withIsEmbedded(false)
                .withUtcTimeZone(mysqlUseUtcTimezone)
                .withDatabaseUrl(mysqlUrl)
                .withDatabaseUsername(mysqlUsername)
                .withDatabasePassword(mysqlPassword);

        return DataSourceHelper.getDataSource(config);
    }

    @Configuration
    @MapperScan(basePackageClasses = VendorMapper.class, sqlSessionFactoryRef = "mysqlSessionFactory")
    public static class MapperScanConfiguration {

    }

    @Bean
    public SqlSessionFactoryBean mysqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new MySqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeHandlersPackage(DbStatusTypeHandler.class.getPackage().getName());

        Properties properties = new Properties();
        sqlSessionFactoryBean.setConfigurationProperties(properties);

        return sqlSessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate mysqlTransactionTemplate(
            @Qualifier("mysqlTransactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    private static class MySqlSessionFactoryBean extends SqlSessionFactoryBean {

        @Override
        protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
            SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
            sqlSessionFactory.getConfiguration().setDefaultScriptingLanguage(Driver.class);
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
            return sqlSessionFactory;
        }

    }

}
