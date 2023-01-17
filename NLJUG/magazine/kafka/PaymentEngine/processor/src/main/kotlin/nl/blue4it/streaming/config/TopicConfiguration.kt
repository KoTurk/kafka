package nl.blue4it.streaming.config

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import java.util.Map


@Configuration
open class TopicConfiguration {

//    @Bean
//    open fun admin(): KafkaAdmin? {
//        return KafkaAdmin(getKafkaProperties())
//    }
//
//    private fun getKafkaProperties(): kotlin.collections.Map<String, Any> = mapOf(
//        Pair("schema.registry.url", "http://localhost:8081"),
//        Pair("bootstrap.servers", "http://localhost:9092"),
//        Pair("specific.avro.reader", "true"),
//        Pair("auto.register.schemas", "true"),
//        Pair("default.key.serde", SpecificAvroSerde::class.java),
//        Pair("default.value.serde", SpecificAvroSerde::class.java),
//    )

    @Bean
    open fun input(): NewTopic {
        return TopicBuilder.name("input-topic")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

    @Bean
    open fun output(): NewTopic {
        return TopicBuilder.name("count")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }
}