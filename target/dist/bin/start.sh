#!/bin/sh

RUNNING_USER=root

JAVA=java

echo "JAVA_HOME: $JAVA"

APP_HOME=$(cd `dirname $0`; pwd)

APP_HOME=${APP_HOME%bin}lib

APP_PARAM=$2

echo "APP_HOME: $APP_HOME"

APP_JAR=listwordjmeter-1.0.0.jar

APP_MAINCLASS=$APP_HOME/${APP_JAR}

JAVA_OPTS="-server -Xmx4096m -Xms4096m -Djava.awt.headless=true  -Djava.security.egd=file:/dev/./urandom"

psid=0

checkpid()
{
javaps=`/bin/ps -ef |grep java |grep ${APP_MAINCLASS%-*}`
if [ -n "$javaps" ]; then
psid=`echo $javaps | awk '{print $2}'`
else
psid=0
fi
echo "psid=$psid"
}


start()
{
checkpid
if [ $psid -ne 0 ]; then
echo "================================"
echo "warn: $APP_MAINCLASS already started! (pid=$psid)"
echo "================================"
else
echo "Starting $APP_MAINCLASS ..."
#JAVA_CMD="nohup $JAVA -jar $JAVA_OPTS  $APP_MAINCLASS  $APP_PARAM > /app/logs/service/listwordjmeter-1.0.0.jar.log 2>&1 </dev/null &"
echo "Start conmand: $JAVA_CMD"
nohup $JAVA -jar $JAVA_OPTS  $APP_MAINCLASS  $APP_PARAM > logs/${APP_JAR%-*}.log 2>&1 </dev/null &
#su -c "$JAVA_CMD"
checkpid
if [ $psid -ne 0 ]; then
echo "(pid=$psid) [OK]"
else
echo "[Failed]"
fi
fi
}

stop()
{
checkpid
echo "stop process id is $psid"
if [ $psid -ne 0 ]; then
echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
kill -9 $psid
if [ $? -eq 0 ]; then
echo "[OK]"
else
echo "[Failed]"
fi
checkpid
if [ $psid -ne 0 ]; then
 stop
fi
else
echo "================================"
echo "warn: $APP_MAINCLASS is not running"
echo "================================"
fi
}

status()
{
checkpid
if [ $psid -ne 0 ];  then
echo "$APP_MAINCLASS is running! (pid=$psid)"
else
echo "$APP_MAINCLASS is not running"
fi
}

info()
{
echo "System Information:"
echo "****************************"
echo `head -n 1 /etc/issue`
echo `uname -a`
echo
echo "JAVA_HOME=$JAVA_HOME"
echo `$JAVA_HOME/bin/java -version`
echo
echo "APP_HOME=$APP_MAINCLASS"
echo "****************************"
}


#if [ -z $APP_PARAM ];then
# echo "Usage: config-url is not set"
# exit 1
#fi


case "$1" in
'start')
start
;;
'stop')
stop
;;
'restart')
stop
start
;;
'status')
status
;;
'info')
info
;;
*)
echo "Usage: $0 {start|stop|restart|status|info}"
exit 1
esac
exit 0