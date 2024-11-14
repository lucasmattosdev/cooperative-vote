package dev.lucasmattos.cooperative_vote.infra.config.queue;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class QueueConfig implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Bean
    public QueueClient defaultQueueClient() {
        return null;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) {
        Binder.get(environment)
                .bind("queue", QueueProperties.class)
                .get()
                .getTopics()
                .forEach((name, props) -> configurableListableBeanFactory.registerSingleton(
                        name + "QueueClient", new MockedQueueClient(props)));
    }
}
