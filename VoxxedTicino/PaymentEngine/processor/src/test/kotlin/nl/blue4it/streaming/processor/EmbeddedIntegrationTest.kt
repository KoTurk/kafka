package nl.blue4it.streaming.processor

import example.avro.CustomerId
import example.avro.Message
import nl.blue4it.streaming.ProcessorApplication
import nl.blue4it.streaming.processor.consumer.CountConsumer
import nl.blue4it.streaming.processor.producer.TransactionProducer
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration

@SpringBootTest(classes = [ProcessorApplication::class])
@ActiveProfiles("test")
internal class EmbeddedIntegrationTest {

    @Autowired
    private val transactionProducer: TransactionProducer? = null

    @Autowired
    private val countConsumer: CountConsumer? = null

    @Test
    fun testConsumer() {
        transactionProducer!!.process(customerId, message)

        Awaitility.await()
            .atMost(Duration.ofSeconds(20))
            .untilAsserted {
                Assertions.assertThat(countConsumer!!.value).isNotNull
                println("received key: " + countConsumer.key)
                println("with count: " + countConsumer.value)
            }
    }

    private val customerId: CustomerId
        private get() = CustomerId.newBuilder()
            .setName("MyNAme")
            .setIban("NLRABO1234543267")
            .build()

    private val message: Message
        private get() = Message.newBuilder()
            .setName("MyNAme")
            .setToIban("NLRABO1234543266")
            .setFromIban("NLRABO1234543267")
            .setAmount(110f)
            .setDatetime(12345678)
            .setProcessed(false)
            .build()
}