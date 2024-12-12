# Kafka Java examples

These are example of using [Kafka](hhttps://kafka.apache.org/) with [Java](https://www.java.com/en/), loosely based on [ISO20022]](https://www.iso20022.org) payments scenarios.

## Prerequisites

1. Install [Java 23](https://java.com/en/download/) or `brew install java` on Mac OS X, which will install the latest version of Java (23 at the time of writing).
2. Install [Docker](https://docs.docker.com/engine/installation/) if you want to use the Docker'ised version of Kafka (see below), or `brew install docker` on Mac OS X.
3. Set the appropriate environment variable values in `.env`.

## Downloading Kafka

You can download and verify a Kafka distribution with the [get_verfiy_kafka.sh](./get_verify_kafka.sh) script. 

You need the distribution even if you choose to run Kafka in Docker (see below), as the test scripts use scripts in the distribution.

## Starting Kafka

There are two ways to start a Kafka instance, locally, and in [Docker](./start_kafka_docker.sh). For the purposes of these exercises it is easiest to run Kafka locally, so that the properties files can be easily updated in the examples below. 

To run Kafka locally, use the [start_kafka_standalone.sh](./start_kafka_standalone.sh) script to start a standalone Kafka instance, using a Kafka distribution downloaded with the [get_verfiy_kafka.sh](./get_verify_kafka.sh) script.

To run Kafka in Docker, use the [start_kafka_docker.sh](./start_kafka_docker.sh) script to run a single-node Kafka instance. This will work for many of the exercises until you need to change properties for Kafka itself.

## Test the Kafka server

You will need two terminal sessions.

In the first, use the [create_topics.sh](./create_topics.sh) script to create a topic named `payments` with 1 partition and 3 replicas. You can change the partition and replica values later as required. If this step fails, go back and check that you have started Kafka properly (see above).

It won't matter if you try to create the same topic more than once, you will simply get an error message: `Topic 'payments' already exists`

Having created the topic, you can use the [test_producer.sh](./test_producer.sh) to send messages to the `payments` topic. The script will send messages until stopped with a Ctr-C.

In another terminal session, run [test_consumer.sh](./test_consumer.sh). This script will consume from the `payments` topic and print each message it receives. This test will read the messages from the start of the topic each time you run it, so that you can see how Kafka persists messages. You can stop the script with a Ctrl+C.

## File Based Producers

The [file_based_producer](./file_based_producer) runs a file based producer that reads messages from a `test.txt` file and sends them to Kafka. The messages are written to a sink file `test.sink.txt`, and to the `connect-test` topic, using the default configuration.

To test this, you will need two terminal sessions. In one session, run the [file_based_producer](./file_based_producer) script.

In the other session, use these commands to see the messages:

``` bash
cat test.sink.txt
. ./.env; $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning
```

You can use a third terminal session to send more messages through this file pipeline with this script:

```bash
. ./append_payment_message_to_file.sh
```

Note that the `MESSAGE_ID` environment variable is incremented each time you run the script, so that each message is distinct. That is why the script is sourced with the `.`.

Use a Ctrl+C to stop the file based producer script. The test data files will be deleted automatically, and the configuration will be reset to its original state.

## Acknowledgements

[APACHE KAFKA QUICKSTART](https://kafka.apache.org/quickstart)
[Goldman Sachs Developer ISO Guide](https://developer.gs.com/docs/services/transaction-banking/iso-file/)

