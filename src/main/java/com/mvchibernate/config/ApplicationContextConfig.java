package com.mvchibernate.config;

import java.util.Properties;

import javax.servlet.annotation.MultipartConfig;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@ComponentScan("com.mvchibernate")
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
@MultipartConfig
public class ApplicationContextConfig {
	@Autowired
	private Environment environment;

	@Bean(name = "freemarkerConfig")
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer freemarkerConfigurer = new FreeMarkerConfigurer();
		freemarkerConfigurer.setTemplateLoaderPath("/WEB-INF/ftl/");
		return freemarkerConfigurer;
	}

	@Bean(name = "freeMarkerviewResolver")
	public FreeMarkerViewResolver getFreemarkerViewResolver() {
		FreeMarkerViewResolver freeviewResolver = new FreeMarkerViewResolver();
		freeviewResolver.setCache(true);
		freeviewResolver.setPrefix("");
		freeviewResolver.setSuffix(".ftl");
		return freeviewResolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getCommonsMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(20971520); // 20MB
		multipartResolver.setMaxInMemorySize(1048576); // 1MB
		return multipartResolver;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getProperty("jdbc.url"));
		dataSource.setUsername(environment.getProperty("jdbc.username"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));

		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory() throws Exception {

		Properties properties = new Properties();
		// See: ds-hibernate-cfg.properties
		properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.ddl-auto"));
		properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
		properties.put("hibernate.connection.autocommit", environment.getProperty("hibernate.connection.autocommit"));

		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setPackagesToScan(new String[] { "com.mvchibernate.model" });
		factoryBean.setDataSource(getDataSource());
		factoryBean.setHibernateProperties(properties);
		factoryBean.afterPropertiesSet();
		SessionFactory sf = factoryBean.getObject();
		return sf;
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}

}