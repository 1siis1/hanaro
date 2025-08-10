package com.hana7.hanaro.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
	private final OrderRepository orderRepository;

	@Bean
	public Job csvJob(JobRepository jobRepository, Step step) {
		return new JobBuilder("csvJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step)
			.build();
	}

	@Bean
	public Step step(JobRepository jobRepository,
		PlatformTransactionManager transactionManager, OrderRepository orderRepository) {
		return new StepBuilder("csvStep", jobRepository)
			.<Order, Order>chunk(5, transactionManager)
			.reader(orderReader()) // StepScope로 CSV 경로 받음
			.writer(orderWriter(orderRepository))
			.build();
	}

	@Bean
	@StepScope
	protected FlatFileItemReader<Order> orderReader() {
		return new FlatFileItemReaderBuilder<Order>()
			.name("orderReader")
			.resource(new ClassPathResource("orders.csv"))
			.linesToSkip(1)
			.delimited()
			.names("orderText", "state")
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(Order.class);
			}}).build();
	}

	@Bean
	public RepositoryItemWriter<Order> orderWriter(OrderRepository repository) {
		return new RepositoryItemWriterBuilder<Order>()
			.repository(repository)
			.methodName("save")
			.build();
	}

}
