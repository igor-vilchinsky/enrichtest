package com.batch2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.enrichservice.EnrichService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EnrichService.class)
@WebAppConfiguration
public class SpringBatchUnitTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	

	@Test
	public void testLaunchJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

	}

	@Test
	public void testLaunchStep() {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

}
