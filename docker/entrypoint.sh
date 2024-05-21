#!/bin/sh

cd /app

java -cp $(ls | tr '\n' ':') com.example.gomesrodris.archburgers.ArchitectBurgersApiApplication
