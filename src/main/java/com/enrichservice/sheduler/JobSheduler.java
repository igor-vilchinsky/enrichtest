package com.enrichservice.sheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobSheduler {

	private static final Logger log = LoggerFactory.getLogger(JobSheduler.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Scheduled(cron = "*/30 * * * * *")
	private void makeJob() {

		try {
			String dateParam = new Date().toString();
			JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
			JobExecution execution = jobLauncher.run(job, param);
			log.info("Execution of job status:", execution.getStatus());
		} catch (Exception e) {
			log.error("Job execution FAILED", e);
		}
	}
}
