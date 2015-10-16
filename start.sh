#!/bin/bash

java -Xmx768m -Xms384m -Xss512k -XX:+UseCompressedOops -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n -Dport=8181 -Dspring.profiles.active=production -Djava.library.path=$OPENCV_HOME/lib -jar target/opencv-test.jar
