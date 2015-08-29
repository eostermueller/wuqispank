#!/bin/bash
LOG_DIR=$(dirname $0)/logs
if [ -d $LOG_DIR ];
then
   echo "$LOG_DIR has already been created."
else
   mkdir $LOG_DIR
   echo "Created $LOG_DIR "
fi

echo Writing to wuqiSpank logfile $(dirname $0)/logs/wuqiSpank-$$.log
java -jar wuqiSpank.jar -httpPort 8071 1> $(dirname $0)/logs/wuqiSpank-$$.log 2>&1 &
