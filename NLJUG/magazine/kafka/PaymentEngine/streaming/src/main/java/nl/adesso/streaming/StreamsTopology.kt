package nl.adesso.streaming

import example.avro.CustomerId
import example.avro.Message
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Branched
import org.apache.kafka.streams.kstream.KTable
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafkaStreams


@SpringBootApplication
@EnableKafkaStreams
open class StreamsTopology {

    @Bean
    open fun create(builder: StreamsBuilder): Topology {
        val transactions = builder.stream<CustomerId, Message>("input-topic")
        val balances: KTable<CustomerId, Message> = builder.table("balances")
        transactions
            .peek {key, value -> println("Incoming transaction with '$key' and '$value'") }
            .join(balances) { transaction: Message, balance: Message ->
                JoinedMessage(transaction, balance)
            }
            .filter{ _, message -> message.transaction.amount >= message.balance.amount }
            .split()
            .branch(isPossibleFraud(), Branched.withConsumer { ks -> ks.to("fraud") })
            .defaultBranch(Branched.withConsumer { ks -> ks.to("process") })
        return builder.build()
    }

    private fun isPossibleFraud() = { _: CustomerId, _: JoinedMessage -> false }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(StreamsTopology::class.java, *args)
        }
    }
}