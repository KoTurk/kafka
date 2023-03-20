package nl.adesso.streaming;

import example.avro.Account;
import example.avro.Balance;
import example.avro.Fraud;
import example.avro.Payment;
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentEngineTopologyTest {
    private static final String SCHEMA_REGISTRY_SCOPE = PaymentEngineTopologyTest.class.getName();
    private static final String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;

    private TopologyTestDriver testDriver;

    private TestInputTopic<Account, Payment> paymentTopic;
    private TestInputTopic<Account, Balance> balanceTopic;
    private TestOutputTopic<Account, Fraud> fraudTopic;

    @BeforeEach
    void beforeEach() {

        // Create streams builder and use the topology we made
        StreamsBuilder builder = new StreamsBuilder();
        new PaymentEngineTopology().handleStream(builder);
        Topology topology = builder.build();

        // Create test driver
        testDriver = new TopologyTestDriver(topology, createKafkaProperties());

        // Create Serdes used for test record keys and values
        Serde<Account> accountSerde = new SpecificAvroSerde<>();
        Serde<Payment> avroPaymentSerde = new SpecificAvroSerde<>();
        Serde<Fraud> avroFraudSerde = new SpecificAvroSerde<>();
        Serde<Balance> avroBalanceSerde = new SpecificAvroSerde<>();

        // Configure Serdes to use the same mock schema registry URL
        Map<String, String> config = Map.of(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        accountSerde.configure(config, true);
        avroPaymentSerde.configure(config, false);
        avroFraudSerde.configure(config, false);
        avroBalanceSerde.configure(config, false);

        // Define input and output topics to use in tests
        paymentTopic = testDriver.createInputTopic(
                "payments",
                accountSerde.serializer(),
                avroPaymentSerde.serializer());
        balanceTopic = testDriver.createInputTopic(
                "balance",
                accountSerde.serializer(),
                avroBalanceSerde.serializer());
        fraudTopic = testDriver.createOutputTopic(
                "fraud",
                accountSerde.deserializer(),
                avroFraudSerde.deserializer());
    }

    @AfterEach
    void afterEach() {
        testDriver.close();
        MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE);
    }

    @Test
    void shouldTestTopology() throws Exception {
        Payment payment = new Payment("SomeEvilPerson", "NL63ABNA332454654", "NL63RABO332454652", 100F, 500F, true);
        balanceTopic.pipeInput(new Account("MisterBlue", "NL63ABNA332454654"), new Balance(200F, "EUR"));
        paymentTopic.pipeInput(new Account("MisterBlue", "NL63ABNA332454654"), payment);
        assertEquals(new Fraud("NL63ABNA332454654"), fraudTopic.readValue());
    }

    private Properties createKafkaProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        return props;
    }
}