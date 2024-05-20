#!/bin/bash

./mvnw package

if [ $? -ne 0 ]
then
	echo "Package failed"
	exit 1
fi

docker build . -f docker/Dockerfile -t gomesrodris/architect-burgers:0.0.1

