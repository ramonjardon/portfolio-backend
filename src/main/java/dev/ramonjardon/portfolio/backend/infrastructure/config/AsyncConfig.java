package dev.ramonjardon.portfolio.backend.infrastructure.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods=false)
public class AsyncConfig implements WebMvcConfigurer{

    @Value("${app.async.timeout:5000}")
    private long asyncTimeout;

    private final ObjectProvider<AsyncTaskExecutor> executorProvider;

    public AsyncConfig(ObjectProvider<AsyncTaskExecutor> executorProvider) {
        this.executorProvider = executorProvider;
    }

    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        ThreadFactory factory = Thread.ofVirtual()
            .name("portfolio-backend-", 0)
            .factory();
        return new TaskExecutorAdapter(
            Executors.newThreadPerTaskExecutor(factory));
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(asyncTimeout);
        // getObject() se llama aquí, cuando el bean ya está completamente
        // construido y decorado por los BeanPostProcessors de Micrometer
        configurer.setTaskExecutor(executorProvider.getObject());
        configurer.registerCallableInterceptors(
            new TimeoutCallableProcessingInterceptor());
    }
}
