package nl.blue4it.streaming

import example.avro.CustomerId
import example.avro.Message
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import nl.blue4it.streaming.processor.TransactionCountProcessor
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.state.Stores
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableKafkaStreams
class ProcessorTopology(private var url: String = "http://localhost:8081",) {

    @Bean
    fun buildTopology(streamsBuilder: StreamsBuilder): Topology {
        println("building topology")

        return streamsBuilder.build()
            .addSource("Source", getCustomerIdSerde().deserializer(), getMessageSerde().deserializer(), "input-topic")
            .addProcessor<CustomerId, Message, CustomerId, Int>("Process", { TransactionCountProcessor() }, "Source")
            .addStateStore(
                Stores.keyValueStoreBuilder(
                    Stores.inMemoryKeyValueStore("TransactionOverviewStore"),
                    getCustomerIdSerde(),
                    Serdes.Integer()
                ),"Process"
            )
            .addSink("Sink", "count", getCustomerIdSerde().serializer(), IntegerSerializer(), "Process")
    }

    private fun getCustomerIdSerde(): SpecificAvroSerde<CustomerId> {
        val customerIdSerde: SpecificAvroSerde<CustomerId> = SpecificAvroSerde<CustomerId>()
        customerIdSerde.configure(getSchemaRegistryUrl(),true)
        return customerIdSerde
    }

    private fun getMessageSerde(): SpecificAvroSerde<Message> {
        val messageSpecificAvroSerde: SpecificAvroSerde<Message> = SpecificAvroSerde<Message>()
        messageSpecificAvroSerde.configure(getSchemaRegistryUrl(),false)
        return messageSpecificAvroSerde
    }

    private fun getSchemaRegistryUrl(): Map<String, *> = mapOf(
        Pair("schema.registry.url", url),
        Pair("bootstrap.servers", "http://localhost:9092"),
        Pair("specific.avro.reader", "true"),
        Pair("auto.register.schemas", "true"),
        Pair("default.key.serde", SpecificAvroSerde::class.java),
        Pair("default.value.serde", SpecificAvroSerde::class.java),
    )
}