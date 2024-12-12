#!/bin/bash

. ./.env

"$KAFKA_HOME"/bin/kafka-console-consumer.sh --topic "$TOPIC" --from-beginning --bootstrap-server "$KAFKA_SERVER"