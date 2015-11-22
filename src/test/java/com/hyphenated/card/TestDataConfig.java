package com.hyphenated.card;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Nitin on 08-11-2015.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.hyphenated.card.repos")
@EnableTransactionManagement
class TestDataConfig {

    @Bean(name = "transactionManager")
    @Autowired
    public PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.hyphenated.card.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }

    @Bean
    public EntityManager entityManager(HibernateEntityManagerFactory entityManagerFactory) {
        HibernateEntityManager entityManager = (HibernateEntityManager) entityManagerFactory.createEntityManager();
        return entityManager;
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();
        prop.put("hibernate.show_sql", "false");
        prop.put("hibernate.hbm2ddl.auto", "create");
        prop.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        return prop;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
//                .addScript("classpath:com/bank/config/sql/schema.sql")
//                .addScript("classpath:com/bank/config/sql/test-data.sql")
                .build();
    }
}