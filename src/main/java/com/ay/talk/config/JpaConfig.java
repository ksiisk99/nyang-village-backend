package com.ay.talk.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableJpaRepositories(basePackages = "com.ay.talk.jparepository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@PropertySource("classpath:application.properties")
public class JpaConfig {

	@Value("${spring.datasource.url}")
	String dbUrl;
	@Value("${spring.datasource.username}")
	String dbUserName;
	@Value("${spring.datasource.password}")
	String dbPassword;
	@Value("${spring.datasource.driver.class.name}")
	String dbDriverClassName;
	@Value("${spring.jpa.database.platform}")
	String jpadatabasePlatform;
	
	public @Bean DataSource dataSource() {
		DriverManagerDataSource dataSource=new DriverManagerDataSource();
		dataSource.setDriverClassName(dbDriverClassName);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUserName);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}
	
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setDataSource(dataSource());
	    factory.setPackagesToScan("com.ay.talk.jpaentity");
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setJpaProperties(jpaProperties());
	    
	    return factory;
	}
		
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager=new JpaTransactionManager(entityManagerFactory.getObject());
		transactionManager.setNestedTransactionAllowed(true);
		return transactionManager;
	}
	
	public Properties jpaProperties() {
		 Properties properties=new Properties();
		 properties.setProperty("logging.level.org.hibernate.SQL", "ERROR");
		 properties.setProperty("hibernate.format_sql", "true");
		 properties.setProperty("hibernate.show_sql", "true");
		 properties.setProperty("hibernate.hbm2ddl.auto","validate");
		 properties.setProperty("hibernate.dialect", jpadatabasePlatform);
		return properties;
	}
}
