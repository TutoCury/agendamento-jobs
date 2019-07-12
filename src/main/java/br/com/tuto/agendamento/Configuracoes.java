package br.com.tuto.agendamento;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Configuration
public class Configuracoes implements SchedulingConfigurer {

    @Bean
    public TaskScheduler poolScheduler() {
	ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	scheduler.setThreadNamePrefix("AgendadorTP");
	scheduler.setPoolSize(10);
	scheduler.initialize();
	return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	taskRegistrar.setScheduler(poolScheduler());
    }

    @Bean
    public RestTemplate restTemplate() {
	return new RestTemplate();
    }

}
