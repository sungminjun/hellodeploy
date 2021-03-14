#!/bin/sh
##set -e ## exit if fail 
##set -x ## shows all command run
# run with sudo
if ! [ $(id -u) = 0 ]; then
	echo "The script need to be run as root." >&2
	exit 1
fi
# and check free memory
FREEMEM=$(free | grep Mem | awk {'print $7'})
if [ ${FREEMEM} -gt 400000 ]; then
	echo Free memory: ${FREEMEM}
else
	echo not enough memory.; exit 1;
fi

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
./gradlew -Dorg.gradle.jvmargs=-Xmx256M --no-daemon clean test && echo test done. || (echo test fail.; exit 1;)

# grep port num. 
## this file under version control. so git pull first and check build success
TARGET_WEBAPP_PORT_FILE_PATH=./src/main/resources/application.properties
## replace 
grep server.port ${TARGET_WEBAPP_PORT_FILE_PATH}
if [[ x${BLUE} == x${CURRENT_PORT} ]]; then
	sed -i 's/server.port\=8081/server.port\=8082/' ${TARGET_WEBAPP_PORT_FILE_PATH}
else
	sed -i 's/server.port\=8082/server.port\=8081/' ${TARGET_WEBAPP_PORT_FILE_PATH}
fi
grep server.port ${TARGET_WEBAPP_PORT_FILE_PATH}

# now build jar
./gradlew -Dorg.gradle.jvmorgs=-Xmx256M --no-daemon build
JAR_FN=$(basename $(ls ./build/libs/*.jar)) && echo jar file: ${JAR_FN} 
JAR_PATH=./build/libs/
if [ x"$EXPECTED_JAR_FN" != x"$JAR_FN" ]; then
	echo file name not matched. expected: ${EXPECTED_JAR_FN} but ${JAR_FN} && exit 1;
fi

# run jar
CURRENT_PID=$(ps auxf | grep 'bluegreen.jar' | grep -v grep | awk {'print $2'})
echo CURRENT_PID is ${CURRENT_PID}
java -jar ${JAR_PATH}/${JAR_FN} &
sleep 90s

# should check new prog running well tho...
RUNNING=$(curl -s 0.0.0.0:${FUTURE} | grep status | wc -l)
if [[ RUNNING -eq 1 ]]; then
	echo run successfully.
else 
	echo run fail after 90s; exit 1;
fi

# swap nginx conf
if [[ ${CURRENT_PORT} -eq ${BLUE} ]]; then
	rm /etc/nginx/nginx.conf && ln -s /etc/nginx/nginx.conf.8082 /etc/nginx/nginx.conf && echo set 8082 nginx.conf
else
	rm /etc/nginx/nginx.conf && ln -s /etc/nginx/nginx.conf.8081 /etc/nginx/nginx.conf && echo set 8081 nginx.conf
fi

# reload nginx
systemctl reload nginx && kill -9 ${CURRENT_PID}

echo ${ProgName} done.

