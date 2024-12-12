#!/bin/bash

# shellcheck disable=SC1091
. ./.env

"$KAFKA_HOME"/bin/kafka-topics.sh --create --topic "$TOPIC" --bootstrap-server "$KAFKA_SERVER" --replication-factor 1 --partitions 3
"$KAFKA_HOME"/bin/kafka-topics.sh --describe --topic "$TOPIC" --bootstrap-server "$KAFKA_SERVER"