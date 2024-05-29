package com.wsmt.chatapi;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    private final TaskExecutor taskExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (taskExecutor instanceof ThreadPoolTaskExecutor) {
            ((ThreadPoolTaskExecutor) taskExecutor).shutdown();
        }
    }
}
