package com.enrichservice.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.enrichservice.model.Employee;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeItemProcessor.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${enrich.url}")
	private String employeeDataUrl;

	@Override
	public Employee process(final Employee employee) throws Exception {
		Long employeeId = employee.getId();
		final Employee transformedEmployee;
		try {
			log.info("Obtaining data about the employee with id " + employeeId);
			transformedEmployee = restTemplate.getForObject(employeeDataUrl + employeeId, Employee.class, employeeId);
			log.info("Employee data obtained from url " + employeeDataUrl + employeeId);
			transformedEmployee.setId(employeeId);
		} catch (Exception ex) {
			log.error("Unable to recieve data from rest service {}", employeeId, ex);
			return employee;
		}
		log.info("Converting (" + employee + ") into (" + transformedEmployee + ")");
		return transformedEmployee;
	}
}
