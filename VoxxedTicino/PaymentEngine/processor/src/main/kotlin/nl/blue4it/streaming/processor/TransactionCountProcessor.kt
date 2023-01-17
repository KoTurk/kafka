package nl.blue4it.streaming.processor

import example.avro.CustomerId
import example.avro.Message
import org.apache.kafka.streams.processor.PunctuationType
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.KeyValueStore
import java.time.Duration

/*
 The processor will count all input-topic of a customer.
 The code will only be executed when there is a message in the input-topic.
 Please goto http://localhost:8080/ui/clusters/local/topics/input-topic,
 next goto tab messages and click on button "Produce Message",
 fill in the key and content and click on send.
*/
class TransactionCountProcessor : Processor<CustomerId, Message, CustomerId, Int> {
    private lateinit var kvStore: KeyValueStore<CustomerId, Int>

    override fun init(context: ProcessorContext<CustomerId, Int>) {
       // 7 add puntuator

        kvStore = context.getStateStore("TransactionOverviewStore")
    }

    override fun process(record: Record<CustomerId, Message>) {
        println("Counting transactions by customer")

        val oldValue: Int? = kvStore[record.key()]
        if (oldValue == null) {
            kvStore.put(record.key(), 1)
        } else {
            kvStore.put(record.key(), oldValue + 1)
        }
    }

    override fun close() {}
}