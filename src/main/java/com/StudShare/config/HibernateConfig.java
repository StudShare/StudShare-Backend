package com.StudShare.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import javax.sql.DataSource;

@Configuration
public class HibernateConfig
{
    @Value("${jdbc.driverClassName}")
    private String DRIVER_CLASS_NAME;
    @Value("${jdbc.url}")
    private String URL;
    @Value("${jdbc.databaseName}")
    private String DATABASE_NAME;
    @Value("${jdbc.username}")
    private String USERNAME;
    @Value("${jdbc.password}")
    private String PASSWORD;
    @Value("${hibernate.dialect}")
    private String DIALECT;
    @Value("${hibernate.show_sql}")
    private String SHOW_SQL;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean(name = "dataSource")
    public DataSource getDataSource()
    {

//        System.out.println("\n\n\n\n\n\n");
//        System.out.println(DRIVER_CLASS_NAME + " "+ URL + " " + DATABASE_NAME + " " + USERNAME + " " + PASSWORD + " " + DIALECT + " " + SHOW_SQL);
//        System.out.println("\n\n\n\n\n\n");

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL + DATABASE_NAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory()
    {

        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(getDataSource());
        //sessionBuilder.setProperty("hibernate.dialect", DIALECT);
        //sessionBuilder.setProperty("hibernate.show_sql", SHOW_SQL);

        sessionBuilder.scanPackages("com.StudShare.domain");

        return sessionBuilder.buildSessionFactory();
    }


    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager()
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(getSessionFactory());

        return transactionManager;
    }
}
