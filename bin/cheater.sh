#!/bin/sh

cygwin=`uname | grep -i cygwin`
separator=";"

if [ -z "$cygwin" ]; then
  separator=":"
fi

bin=`dirname $0`
app=$bin/../

classpath=$CLASSPATH
classpath="$classpath$separator$app/lib/commons-codec-1.3.jar"
classpath="$classpath$separator$app/lib/commons-httpclient-3.1.jar"
classpath="$classpath$separator$app/lib/commons-logging-1.1.1.jar"
classpath="$classpath$separator$app/lib/log4j-1.2.15.jar"
classpath="$classpath$separator$app/etc"
classpath="$classpath$separator$app/bin/cheater-0.5.1.jar"

CLASSPATH="$classpath" java com.honnix.cheater.cli.Main
