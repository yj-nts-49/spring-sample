package com.sios.ex.springbatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.sios.ex"})
@EnableJpaRepositories(basePackages = {"com.sios.ex"})
@EntityScan(basePackages = {"com.sios.ex"})
public class SpringBatchSample1Configuration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SourceItemRepository sourceItemRepository;
	
	@Autowired
	private TargetItemRepository targetItemRepository;
	
	@Autowired
	private LargeItemRepository largeItemRepository;
	
	/*
	 * Step-1
	 */
	
	@Bean
	public ItemReader<SourceItemEntity> sourceItemReader() {
		RepositoryItemReader<SourceItemEntity> reader = new RepositoryItemReader<>();
		reader.setRepository(sourceItemRepository);
		reader.setMethodName("findAll");
		Map<String, Direction> sort = new HashMap<>();
		sort.put("name", Direction.ASC);
		reader.setSort(sort);
		return reader;
	}
	
	@Bean
	public TargetItemProcessor targetItemProcessor() {
		return new TargetItemProcessor();
	}
	
	@Bean
	public ItemWriter<TargetItemEntity> targetItemWriter() {
		RepositoryItemWriter<TargetItemEntity> writer = new RepositoryItemWriter<>();
		writer.setRepository(targetItemRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Step sampleBatch1Step1() {
		return stepBuilderFactory.get("sampleBatch1Step1")
				.<SourceItemEntity, TargetItemEntity> chunk(Integer.MAX_VALUE)
				.reader(sourceItemReader())
				.processor(targetItemProcessor())
				.writer(targetItemWriter())
				.build();
	}
	
	/*
	 * Step-2
	 */
	
	@Bean
	public ItemReader<TargetItemEntity> targetItemReader() {
		RepositoryItemReader<TargetItemEntity> reader = new RepositoryItemReader<>();
		reader.setRepository(targetItemRepository);
		reader.setMethodName("findAll");
		Map<String, Direction> sort = new HashMap<>();
		sort.put("name", Direction.ASC);
		reader.setSort(sort);
		return reader;
	}
	
	@Bean
	public LargeItemProcessor largeItemProcessor() {
		return new LargeItemProcessor();
	}
	
	@Bean
	public ItemWriter<LargeItemEntity> largeItemWriter() {
		RepositoryItemWriter<LargeItemEntity> writer = new RepositoryItemWriter<>();
		writer.setRepository(largeItemRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Step sampleBatch1Step2() {
		return stepBuilderFactory.get("sampleBatch1Step2")
				.<TargetItemEntity, LargeItemEntity> chunk(Integer.MAX_VALUE)
				.reader(targetItemReader())
				.processor(largeItemProcessor())
				.writer(largeItemWriter())
				.build();
	}
	
	/*
	 * Job
	 */
	@Bean
	public Job sampleBatch1Job() {
		return jobBuilderFactory.get("sampleBatch1Job")
				.incrementer(new RunIdIncrementer())
				.flow(sampleBatch1Step1())
				.next(sampleBatch1Step2())
				.end()
				.build();
	}

}
