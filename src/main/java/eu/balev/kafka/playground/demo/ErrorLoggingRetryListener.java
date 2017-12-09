package eu.balev.kafka.playground.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

public class ErrorLoggingRetryListener extends RetryListenerSupport {
private static final Logger LOGGER = LoggerFactory.getLogger(ErrorLoggingRetryListener.class);

    @Override
    public <T, E extends Throwable> void onError(final RetryContext context, final RetryCallback<T, E> callback,
                                                 final Throwable throwable) {
        try {
            final ConsumerRecord record = (ConsumerRecord) context.getAttribute("record");
            LOGGER.error("Could not process {} message with key {} on partition {} with offset {}, tried {} times.",
                        record.topic(), record.key(), record.partition(), record.offset(), context.getRetryCount() + 1, throwable.getCause());

        } catch (RuntimeException e) {
            LOGGER.warn("Caught exception in retry handler!", e);
        }
    }
}