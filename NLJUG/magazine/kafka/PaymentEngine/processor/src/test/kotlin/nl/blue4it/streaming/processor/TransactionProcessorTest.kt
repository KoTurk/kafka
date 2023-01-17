package nl.blue4it.streaming.processor

import com.google.gson.Gson
import example.avro.CustomerId
import example.avro.Message

import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import nl.blue4it.streaming.ProcessorTopology
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TransactionProcessorTest {
    private var testDriver: TopologyTestDriver? = null
    private var inputTopic: TestInputTopic<CustomerId, Message>? = null
    private var outputTopic: TestOutputTopic<CustomerId, Int>? = null

    @BeforeEach
    @Throws(Exception::class)
    fun beforeEach() {
        val builder = StreamsBuilder()
        val topology = ProcessorTopology(MOCK_SCHEMA_REGISTRY_URL).buildTopology(builder)

       // val topology: Topology = builder.build()

        // Dummy properties needed for test diver
        val props = Properties()
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL)

        // Create test driver
        testDriver = TopologyTestDriver(topology, props)
        val customerIdSerde: Serde<CustomerId> = SpecificAvroSerde()
        val messageSerde: Serde<Message> = SpecificAvroSerde()

        // Configure Serdes to use the same mock schema registry URL
        val config: Map<String, String> = mapOf(
            Pair(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL)
        )
        customerIdSerde.configure(config, true)
        messageSerde.configure(config, false)

        // Define input and output topics to use in tests
        inputTopic = testDriver!!.createInputTopic(
            "input-topic",
            customerIdSerde.serializer(),
            messageSerde.serializer()
        )
        outputTopic = testDriver!!.createOutputTopic(
            "count",
            customerIdSerde.deserializer(),
            IntegerDeserializer()
        )
    }

    @AfterEach
    fun afterEach() {
        testDriver!!.close()
        MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testOneTransactionCount() {
        val customer = CustomerId("Mister X", "NL71BANK0332454676")
        val message = Message("", "NL71BANK0332454676", "NL71BANK0332454678", 200F, 1232322112L, true)

//        val customerJson: String = Gson().toJson(customer)
//        val messageJson: String = Gson().toJson(message)
//        println(customerJson)
//        println(messageJson)

        inputTopic!!.pipeInput(customer, message)
        Thread.sleep(2000)
        //Read and validate output to match word as key and count as value
        assertThat(outputTopic!!.readValue()).isEqualTo(1)
        assertThat(outputTopic!!.isEmpty).isTrue
    }

    companion object {
        private val SCHEMA_REGISTRY_SCOPE: String = TransactionProcessorTest::class.java.getName()
        private val MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE
    }
}