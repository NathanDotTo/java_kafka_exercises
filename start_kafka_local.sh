#!/bin/bash

. ./.env

rm -rf /tmp/kraft-combined-logs
export KAFKA_CLUSTER_ID="$("$KAFKA_HOME"/bin/kafka-storage.sh random-uuid)"
"$KAFKA_HOME"/bin/kafka-storage.sh format -t "$KAFKA_CLUSTER_ID" -c "$KAFKA_HOME"/config/kraft/server.properties
# Using KRaft instead of Zookeeper
"$KAFKA_HOME"/bin/kafka-server-start.sh "$KAFKA_HOME"/config/kraft/server.properties