package nl.adesso.streaming.streams;

import example.avro.Account;
import example.avro.Balance;
import example.avro.Fraud;
import example.avro.Payment;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class PaymentKStreams {

    public static KStream<Account, Payment> getPaymentKStream(StreamsBuilder builder) {
        // 4.1 Create stream payments

        // 4.8 create table balances

        // 4.2 peek and create metrics

        // 4.9 join balance

        // 4.3 filter balance and amount

        // 4.4 mapValues

        // 4.5 peek balance ok

        // 4.6 to topic fraud

        // 4.7 return stream
        return null;
    }

    private static Payment enhancedPayment(Payment payment, Balance balance) {
        return new Payment(payment.getName(), payment.getIban(), payment.getToIban(), payment.getAmount(), balance.getAmount(), payment.getProcessed());
    }

}
