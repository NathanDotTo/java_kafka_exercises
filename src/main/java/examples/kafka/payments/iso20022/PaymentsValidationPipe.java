/*
Copyright 2024 Nathan Sowatskey (<nathan at nathan.to>)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package examples.kafka.payments.iso20022;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import java.util.Properties;

/**
 * In this example, we implement a simple Pipe to accept payments instructions
 * from a Kafka topic, validate them, and then write the valid payments to
 * another topic.
 */
public class PaymentsValidationPipe {

    public static void main(String[] args) {
        // Configure properties for the Streams application
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "payments-validation-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Create a StreamsBuilder
        final StreamsBuilder builder = new StreamsBuilder();

        // Define the input topic
        KStream<String, String> paymentsStream = builder.stream("payment-instructions");

        // Split the stream based on validation using split() and named branches
        paymentsStream.split()
                .branch((key, value) -> isValidFormat(value),
                        Branched.withConsumer((ks) -> ks.to("valid-payments")))
                .branch((key, value) -> !isValidFormat(value),
                        Branched.withConsumer((ks) -> ks.to("invalid-payments")));

        // Build application
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // Add a shutdown hook to gracefully close the Streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // Start the application
        try {
            streams.start();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    private static boolean isValidFormat(String payment) {
        // Check the format of the payload
        try {
            // Validate XML here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
