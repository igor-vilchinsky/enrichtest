package com.enrichservice.processor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.enrichservice.EnrichService;
import com.enrichservice.model.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EnrichService.class)
public class EmployeeItemProcessorTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private EmployeeItemProcessor employeeItemProcessor;

	private Employee mockEmployee;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockEmployee = new Employee(1l, "testName", 10000.0);
	}

	@Test
	public void testProcess() throws Exception {
		Employee expectedEmployee = new Employee(1l, "testName", 10000.0);
		when(restTemplate.getForObject(any(String.class), any(Class.class), any(Long.class))).thenReturn(mockEmployee);
		Employee employeeBeforeEnrich = new Employee(1l, null, null);
		assertEquals(expectedEmployee, employeeItemProcessor.process(employeeBeforeEnrich));
	}

}
