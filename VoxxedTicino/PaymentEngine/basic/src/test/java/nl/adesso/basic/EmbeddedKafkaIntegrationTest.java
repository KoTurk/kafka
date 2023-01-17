package nl.adesso.basic;


import static org.assertj.core.api.Assertions.assertThat;

import example.avro.Balance;
import example.avro.Payment;

import lombok.extern.slf4j.Slf4j;
import nl.adesso.basic.kafka.producer.BalanceProducer;
import nl.adesso.basic.kafka.producer.PaymentProducer;
import nl.adesso.basic.kafka.consumer.PaymentConsumer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PaymentEngineApplication.class)
@Slf4j
class EmbeddedKafkaIntegrationTest {

    @Autowired private PaymentProducer paymentProducer;

    @Autowired private BalanceProducer balanceProducer;

    @Autowired private PaymentConsumer paymentConsumer;

    @Test
    public void testConsumer() throws Exception {
        paymentProducer.processPayment(createPayment());
        balanceProducer.processBalance(createBalance());

        Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> {
                    assertThat(paymentConsumer.getPayment()).isNotNull();
                    assertTrue(paymentConsumer.getPayment().getProcessed());
                    log.info("--------> Payment send to topic, proceed");
                   });
    }

    private Payment createPayment() {
        return Payment.newBuilder()
                .setName("Jantje")
                .setAmount(200.00F)
                .setBalance(400.00F)
                .setIban("BL63ABNA332454654")
                .setToIban("BL61RABO0332543675")
                .setProcessed(true)
                .build();
    }

    private Balance createBalance() {
        return Balance.newBuilder()
                .setAmount(900.00F)
                .setCurrency("BLA")
                .build();
    }

}
