#!/bin/bash

. ./.env

docker pull apache/kafka:"$KAFKA_VERSION"
docker run -p 9092:9092 apache/kafka:"$KAFKA_VERSION"