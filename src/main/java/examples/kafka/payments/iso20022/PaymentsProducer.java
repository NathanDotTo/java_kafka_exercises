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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * In this example, we implement a simple Producer to send payments instructions
 * from an XML template to a Kafka topic.
 */
public class PaymentsProducer {

    public static void main(String[] args) {
        // Configure Kafka producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your broker address
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Specify the XML template file path
        String xmlFilePath = "sample_data/CHAPS/pain.001.001.003.xml";

        try {
            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlFilePath));
            Element rootElement = document.getDocumentElement();

            NodeList msgIdNodes = document.getElementsByTagName("MsgId");
            for (int msgId = 20; msgId < 30; msgId++) {
                if (msgIdNodes.getLength() > 0) {
                    Element msgIdElement = (Element) msgIdNodes.item(0);

                    // Set the text content of the <MsgId> element
                    msgIdElement.setTextContent(String.valueOf(msgId)); // TODO use a GUID
                } else {
                    System.err.println("Error: <MsgId> element not found."); // TODO use logging/OTel
                }

                String paymentMessage = rootElement.getTextContent();
                System.out.println(paymentMessage);
                
                // Publish the message to the Kafka topic
                producer.send(new ProducerRecord<>("payment-instructions", paymentMessage)); //TODO read topic from .env
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Error reading or parsing XML: " + e.getMessage());
        } finally {
            // Close the producer
            producer.close();
        }
    }
}
