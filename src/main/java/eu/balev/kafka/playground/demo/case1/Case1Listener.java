package eu.balev.kafka.playground.demo.case1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "test-topic-case1")
@ConditionalOnProperty(name = "demo.case", havingValue = "case1")
public class Case1Listener {

  private static final Logger LOGGER = LoggerFactory.getLogger(Case1Listener.class);

   @KafkaHandler
  public void onMessage(final String message,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic,
                        final Acknowledgment acknowledgment) {
      if ("crash".equals(message))
      {
        throw new IllegalArgumentException("We crash when we see 'crash'!");
      }
      else
      {
        LOGGER.info("Received message {}.", message);
      }

      acknowledgment.acknowledge();

  }
}
