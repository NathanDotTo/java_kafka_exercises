#!/bin/bash

# shellcheck disable=SC1091
. ./.env

rm -rf "$KAFKA_HOME"

KAFKA_URL="https://dlcdn.apache.org/kafka/$KAFKA_VERSION/kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz"
KAFKA_FILE_TGZ="kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz"
KAFKA_FILE_TAR="kafka_$SCALA_VERSION-$KAFKA_VERSION.tar"
KAFKA_URL_ASC="https://dlcdn.apache.org/kafka/$KAFKA_VERSION/kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz.asc"
KAFKA_KEYS_URL=https://dlcdn.apache.org/kafka/KEYS

echo "Downloading from $KAFKA_URL"

curl -O "$KAFKA_URL"
curl -O $KAFKA_KEYS_URL
curl -O "$KAFKA_URL_ASC"

gpg --import KEYS

# See the following explanations to understand the limitations here:
# https://www.apache.org/info/verification.html

echo "Verifying $KAFKA_FILE_TGZ"
# TODO fail script if verify fails
gpg --verify "$KAFKA_FILE_TGZ".asc "$KAFKA_FILE_TGZ"

gzip -d "$KAFKA_FILE_TGZ"
tar -xf "$KAFKA_FILE_TAR"
rm "$KAFKA_FILE_TGZ".asc KEYS "$KAFKA_FILE_TAR"
