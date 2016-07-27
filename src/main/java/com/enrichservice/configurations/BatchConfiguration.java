package com.enrichservice.configurations;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import com.enrichservice.model.Employee;
import com.enrichservice.processor.EmployeeItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("${file.path}")
	private String filePath;

	@Value("${enrichfile.path}")
	private String enrichFilePath;

	@Value("${job.title}")
	private String jobTitle;

	@Value("${step.title}")
	private String stepTitle;

	@Value("${field1.name}")
	private String employeeId;

	@Value("${field2.name}")
	private String name;

	@Value("${field3.name}")
	private String salary;

	@Bean
	public FlatFileItemReader<Employee> reader() {
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new PathResource(filePath));
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { employeeId });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public EmployeeItemProcessor processor() {
		return new EmployeeItemProcessor();
	}

	@Bean
	public FlatFileItemWriter<Employee> writer() {
		FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<Employee>();
		writer.setResource(new PathResource(enrichFilePath));
		writer.setShouldDeleteIfExists(true);
		DelimitedLineAggregator<Employee> delLineAgg = new DelimitedLineAggregator<Employee>();
		delLineAgg.setDelimiter(",");
		BeanWrapperFieldExtractor<Employee> fieldExtractor = new BeanWrapperFieldExtractor<Employee>();
		fieldExtractor.setNames(new String[] { employeeId, name, salary });
		delLineAgg.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(delLineAgg);
		return writer;

	}

	@Bean
	public Job fillEmployeeInfoJob() {
		return jobBuilderFactory.get(jobTitle).flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get(stepTitle).<Employee, Employee>chunk(20).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

}