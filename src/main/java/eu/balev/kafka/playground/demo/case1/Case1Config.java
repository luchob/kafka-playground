package eu.balev.kafka.playground.demo.case1;

import eu.balev.kafka.playground.demo.ErrorLoggingRetryListener;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class})
@ConditionalOnProperty(name = "demo.case", havingValue = "case1")
public class Case1Config {

  private final KafkaProperties kafkaProperties;

  @Autowired
  protected Case1Config(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }


  @Bean
  ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    final ContainerProperties containerProperties = factory.getContainerProperties();
    containerProperties.setAckMode(AckMode.MANUAL);
    containerProperties.setAckOnError(false);
    factory.setConcurrency(6);
    factory.setRetryTemplate(retryTemplate());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
  }

  private RetryTemplate retryTemplate() {
    final RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(new SimpleRetryPolicy(Integer.MAX_VALUE,
                                                       Collections.singletonMap(ListenerExecutionFailedException.class, Boolean.TRUE)));
    retryTemplate.setBackOffPolicy(getBackOffPolicy());
    retryTemplate.setThrowLastExceptionOnExhausted(true);
    retryTemplate.registerListener(new ErrorLoggingRetryListener());
    return retryTemplate;
  }

  private BackOffPolicy getBackOffPolicy() {
    final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(5000);
    return fixedBackOffPolicy;
  }

}
