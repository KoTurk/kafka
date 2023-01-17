package nl.adesso.basic;


import example.avro.Payment;
import lombok.extern.slf4j.Slf4j;
import nl.adesso.basic.kafka.consumer.PaymentConsumer;
import nl.adesso.basic.kafka.producer.PaymentProducer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PaymentEngineApplication.class)
@Slf4j
class BigDataMessageSendTest {

    @Autowired
    private PaymentProducer service;

    @Autowired
    private PaymentConsumer paymentProcessor;

    @Test
    public void testConsumer() {

        for(int i = 0; i < 1000; i++){
            service.processPayment(createPayment());
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    assertThat(paymentProcessor.getPayment()).isNotNull();
                    assertTrue(paymentProcessor.getPayment().getProcessed());
                    log.info("--------> Payment send to topic, proceed");
                });
    }

    private Payment createPayment() {
        return Payment.newBuilder()
                .setName("Mister Blue")
                .setAmount(100.00F)
                .setBalance(500.00F)
                .setIban("NL63" + randomIbanCode() + "332454654")
                .setToIban("NL61RABO0332543675")
                .setProcessed(true)
                .build();
    }

    private String randomIbanCode() {
        String[] ibanCodes = {"RABO", "ABNA", "INGB", "DEUT"};
        Random random = new Random();
        int randomNumber = random.nextInt(ibanCodes.length);

        return ibanCodes[randomNumber];
    }

}