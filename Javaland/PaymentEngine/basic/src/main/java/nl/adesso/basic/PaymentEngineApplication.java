package nl.adesso.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication
public class PaymentEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentEngineApplication.class, args);
    }
}
