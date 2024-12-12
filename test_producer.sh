#!/bin/bash

. ./.env

message_num=0
while true; do
    echo "payment_$message_num" | "$KAFKA_HOME"/bin/kafka-console-producer.sh --topic "$TOPIC" --bootstrap-server "$KAFKA_SERVER"
    message_num=$(( message_num + 1 ))
    trap 'echo "Caught SIGINT, exiting..."; exit 1' SIGINT
done