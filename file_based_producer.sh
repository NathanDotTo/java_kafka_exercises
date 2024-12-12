#!/bin/bash

# shellcheck disable=SC1091
. ./.env

# Configure Kafka with a file connector
cp "$KAFKA_HOME"/config/connect-standalone.properties "$KAFKA_HOME"/config/connect-standalone.properties.orig
echo "plugin.path=$KAFKA_HOME/libs/connect-file-3.8.1.jar" >> "$KAFKA_HOME"/config/connect-standalone.properties

# Write an example payment to test.txt
./append_payment_message_to_file.sh

#echo -e "foo\nbar" > test.txt

# To tidy up after
trap 'echo "Caught SIGINT, deleting test files, resetting config, and exiting...";\
 rm test.txt test.sink.txt pain.001.001.003_message.xml;\
  mv "$KAFKA_HOME"/config/connect-standalone.properties.orig "$KAFKA_HOME"/config/connect-standalone.properties;\
   exit 1' SIGINT

# Use the default file source and configuration connectors to read from test.txt and write to test.sink.txt.
"$KAFKA_HOME"/bin/connect-standalone.sh\
 "$KAFKA_HOME"/config/connect-standalone.properties\
  "$KAFKA_HOME"/config/connect-file-source.properties\
   "$KAFKA_HOME"/config/connect-file-sink.properties
