#!/bin/bash
echo "Modified Jason (NO Env) in : "
pwd 

SECONDS_TO_RUN=20  # Number of seconds to run

END_TIME=$((SECONDS_TO_RUN + $(date +%s)))

for i in {0..1};
do
	jason lbb3.mas2j > outp2 &
	sleep 1
	PID_JAS=$(jps | grep RunLocalMAS | awk 'NR==1{print $1}')
	while [ $(date +%s) -lt $END_TIME ] && [ ! -f .stop___MAS ]; do 
	    # Do something while Jason is running (the file doesn't exist)
	    ps -p $PID_JAS -o %cpu 
	    sleep 1
	done
done
python3 test.py 0

