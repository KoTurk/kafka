package nl.adesso.streaming.exception.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomProductionExceptionHandler implements ProductionExceptionHandler {

    public ProductionExceptionHandlerResponse handle(final ProducerRecord<byte[], byte[]> record, final Exception exception) {
        return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> configs) {}
}
