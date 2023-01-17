package nl.adesso.basic.kafka.producer;

import example.avro.Account;
import example.avro.Payment;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import nl.adesso.basic.exception.FilterException;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    // 2.2 Create Kafka <String, Payment> Template
    private final KafkaTemplate<Account, Payment> kafkaTemplate;

    public boolean processPayment(Payment payment) {
        sendTransaction(getAccountFromCookie(), payment);

        return true;
    }

    private void sendTransaction(Account key, Payment payment) {
        log.info("Going to process transaction, sending message {}", payment);

        // 2.4.2 send exception
        if (payment.getIban().equals("NL61EVIL0332546754")) {
            // 2.4.2 create filter exception
            FilterException up = new FilterException("oh no help me");

            Counter.builder("a.message.exception")
                    .tag("exception", up.getMessage())
                    .register(Metrics.globalRegistry)
                    .increment();
            // throw the exception
            throw up;
        }   if (payment.getIban().equals("NL61EVIL0332546754")) {
            // 2.4.2 create filter exception
            FilterException up = new FilterException("oh no help me");

            Counter.builder("a.message.exception")
                    .tag("exception", up.getMessage())
                    .register(Metrics.globalRegistry)
                    .increment();
            // throw the exception
            throw up;
        }


        // 2.4 Send to topic "payments" and send payment
        kafkaTemplate.send("payments", key, payment);
    }

    private Account getAccountFromCookie() {
        return new Account("Mister Blue", "NL63ABNA332454654");
    }
}