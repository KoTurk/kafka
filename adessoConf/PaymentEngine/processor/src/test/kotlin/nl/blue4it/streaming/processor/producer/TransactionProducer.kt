package nl.blue4it.streaming.processor.producer

import example.avro.CustomerId
import example.avro.Message
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class TransactionProducer {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<CustomerId, Message>

    fun process(customerId: CustomerId, message: Message): Boolean {
        kafkaTemplate.send("input-topic", customerId, message)
        return true
    }
}