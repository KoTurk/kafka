package nl.adesso.basic.kafka.producer;

import example.avro.Account;
import example.avro.Balance;
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
public class BalanceProducer {

    private final KafkaTemplate<Account, Balance> kafkaTemplate;

    public boolean processBalance(Balance balance) {
        sendTransaction(getAccountFromCookie(), balance);

        return true;
    }

    private void sendTransaction(Account key, Balance balance) {
        log.info("Going to send balance, sending {}", balance);

        kafkaTemplate.send("balance", key, balance);
    }

    private Account getAccountFromCookie() {
        return new Account("Mister Blue", "NL63ABNA332454654");
    }
}