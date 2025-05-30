package com.ezee.store.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfiguration {
	
	private static final Logger LOGGER = LogManager.getLogger("com.ezee.store.repository");
	
	@Bean
	@Primary
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = null;
		try {
			dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUsername("root");
			dataSource.setPassword("Ezeeinfo@06");
			dataSource.setUrl("jdbc:mysql://localhost:3306/store");
		} catch (Exception e) {
			LOGGER.info("Message: {},/n error: {}", e.getMessage(), e);
		}

		return dataSource;
	}

	@Bean
	@Scope("prototype")
	public <T> List<T> getList() {
		return new ArrayList<>();
	}
}
