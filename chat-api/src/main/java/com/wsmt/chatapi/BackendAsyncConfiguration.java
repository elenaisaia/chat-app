package com.wsmt.chatapi;

import com.wsmt.chatapi.mq.Send;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BackendAsyncConfiguration implements AsyncConfigurer {
    @Override
    @Bean
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        return executor;
    }

    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor, Send send) {
        return args -> executor.execute(send);
    }
}
