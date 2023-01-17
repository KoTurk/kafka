# Event Streaming Applications with Kafka Streams, Spring Kafka and Actuator

<img src="https://pbs.twimg.com/profile_images/1286221718416027648/6w_Y_5vN_400x400.jpg" />

## Prerequisites
- IDE
- Minimal Java 11
- Maven (Optional)
- Docker

**Please ensure you have prepared your machine well in advance of the workshop. Your time during the workshop is valuable, and we want to use it for learning, rather than setting up machines.**

## Preparing your machine for the workshop
- [Install the pre-requisites](#install-the-pre-requisites)
- [Get a copy of this repository](#get-a-copy-of-this-repository)
- [Exercises](#exercises)

### Install the pre-requisites
- Install an IDE like https://www.jetbrains.com/idea/download/
- (Optional) Download Maven https://maven.apache.org/download.cgi and install https://maven.apache.org/install.html
- Download docker https://www.docker.com/products/docker-desktop/

### Get a copy of this repository
Clone or download this repo.

### Note

If you got a missing class and an error for the import of `example.avro.Payment` - then you need to click in IntelliJ IDEA on the project module and select `maven` \ `Generate sources and update folders`. 

### Exercises Basic Kafka
1. Start zookeeper / kafka / kafka-ui with docker with ./start.sh
2. In the src code, follow all the comments and begin from the config to controller, to the producer and consumer
3. When filled in the code, execute the EmbeddedKafkaIntegrationTest

### Exercises Kafka Streams
1. Go to the PaymentKStream, read the comments and fill in the missing code
2. Do the same for the FraudKStream
3. And at last the Topology
4. Now execute the test in directory basic again. 
5. It should pass now. 
6. It is also possible to execute the test from the streaming module (that's in memory and faster)
