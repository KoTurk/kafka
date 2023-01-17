package nl.adesso.basic.controller;

import example.avro.Payment;
import lombok.RequiredArgsConstructor;

import nl.adesso.basic.kafka.producer.PaymentProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentProducer paymentProducer;

    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody Payment payment) {
        if(paymentProducer.processPayment(payment)) {
            return ResponseEntity.ok("Created payment");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
