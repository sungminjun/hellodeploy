#!/bin/sh
set -e
set -x

# begin
ProgName=$0
echo start running ${ProgName}

# blue and green
BLUE=8081
GREEN=8082
NUM_OF_BLUE=$(netstat -an | grep ${BLUE} | wc -l)
NUM_OF_GREEN=$(netstat -an | grep ${GREEN} | wc -l)
echo BLUE ${BLUE} is ${NUM_OF_BLUE}, GREEN: ${GREEN} is ${NUM_OF_GREEN}


# get current and set future
if [ "$NUM_OF_BLUE" -gt "$NUM_OF_GREEN" ]; then 
	CURRENT_PORT=${BLUE}
	FUTURE_PORT=${GREEN}
else
	CURRENT_PORT=${GREEN}
	FUTURE_PORT=${BLUE}
fi
echo CURRENT_PORT is ${CURRENT_PORT}, FUTURE_PORT is ${FUTURE_PORT}

# git pull
EXPECTED_JAR_FN=hellobluegreen.jar
git pull && echo git pull done. || (echo git pull failed.; exit 1;) 
./gradlew clean build && echo build done. || (echo build fail.; exit 1;)
JAR_FN=$(basename $(ls ./build/libs/*.jar)) && echo jar file: ${JAR_FN} 
if [ x"$EXPECTED_JAR_FN" != x"$JAR_FN" ]; then
	echo file name not matched. expected: ${EXPECTED_JAR_FN} but ${JAR_FN} && exit 1;
fi

# grep port num. 
## this file under version control. so git pull first and check build success
TARGET_WEBAPP_PORT_FILE_PATH=./src/main/resources/application.properties
CURRENT_WEBAPP_PORT=$(grep 'server.port' ${TARGET_WEBAPP_PORT_FILE_PATH} | sed 's/server.port=//')
echo ${CURRENT_WEBAPP_PORT}
## replace 
grep server.port ${TARGET_WEBAPP_PORT_FILE_PATH}
if [[ x${BLUE} == x${CURRENT_PORT} ]]; then
	cat ${TARGET_WEBAPP_PORT_FILE_PATH} | sed 's/server.port=8081/server.port=8082/' > ${TARGET_WEBAPP_PORT_FILE_PATH}
else
	cat ${TARGET_WEBAPP_PORT_FILE_PATH} | sed 's/server.port=8082/server.port=8081/' > ${TARGET_WEBAPP_PORT_FILE_PATH}
fi
grep server.port ${TARGET_WEBAPP_PORT_FILE_PATH}

















