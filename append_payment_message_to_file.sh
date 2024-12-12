#!/bin/bash

export MESSAGE_ID=$((MESSAGE_ID + 1))
envsubst < "$DATA_DIR"/pain.001.001.003.xml > pain.001.001.003_message.xml
tr '\n' ' ' < pain.001.001.003_message.xml | sed 's/^[ \t]*//;s/  *//g' >> test.txt
printf '\n' >> test.txt