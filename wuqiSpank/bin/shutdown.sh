ps -ef | grep wuqiSpank | /usr/bin/awk '{print $2}' | xargs -t -n 1 kill -9 1>/dev/null 2>&1
