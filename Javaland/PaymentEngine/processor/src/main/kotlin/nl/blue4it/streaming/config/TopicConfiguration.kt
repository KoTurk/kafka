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