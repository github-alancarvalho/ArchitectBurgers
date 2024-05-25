#!/bin/bash

cd $(dirname $0) || exit 1

cd ..
./mvnw -DskipTests install dependency:copy-dependencies

cd -

rm -rf temp_libs
mkdir temp_libs
cp ../web-api/target/architect-burgers-webapi-*.jar ./temp_libs/ || exit 1
cp ../web-api/target/lib/* ./temp_libs/ || exit 1

if [ $? -ne 0 ]
then
	echo "Package failed"
	exit 1
fi

docker build . -t gomesrodris/architect-burgers:0.0.2

