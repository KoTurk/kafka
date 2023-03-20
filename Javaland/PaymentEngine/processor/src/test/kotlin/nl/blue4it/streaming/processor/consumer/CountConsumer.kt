package nl.blue4it.streaming.processor.consumer

import example.avro.CustomerId
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CountConsumer {
    var key: CustomerId? = null
        private set
    var value: Int? = null
        private set

    @KafkaListener(topics = ["count"], groupId = "something")
    fun consume(consumerRecord: ConsumerRecord<*, *>) {
        val customerId = consumerRecord.key() as CustomerId
        val count = consumerRecord.value() as Int
        println("received payload='{}'$count")
        setValue(count)
        setKey(customerId)
    }

    private fun setKey(customerId: CustomerId) {
        key = customerId
    }

    private fun setValue(count: Int) {
        value = count
    }
}