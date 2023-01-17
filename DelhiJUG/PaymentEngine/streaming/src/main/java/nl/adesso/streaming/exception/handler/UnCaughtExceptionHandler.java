package nl.adesso.streaming.exception.handler;

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

@Component
public class UnCaughtExceptionHandler implements StreamsUncaughtExceptionHandler {

    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {
        // only for retriable and recoverable exceptions, not the null pointers in your code ;)
        // https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Architecture
        return StreamThreadExceptionResponse.REPLACE_THREAD;
    }
}
