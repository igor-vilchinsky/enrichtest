package com.enrichservice.configurations;

import javax.sql.DataSource;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Value("${driver.class}")
	private String driverClass;

	@Value("${datasource.url}")
	private String dataSourceUrl;

	@Value("${datasource.username}")
	private String dataSourceUserName;

	@Value("${datasource.password}")
	private String dataSourcePassword;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(dataSourceUrl);
		dataSource.setUsername(dataSourceUserName);
		dataSource.setPassword(dataSourcePassword);
		return dataSource;
	}

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}
}
