#!/bin/bash
echo "Running Standard Remote Jason "
cp build-gradle.rafael.standardRemoteJason.txt build.gradle 

SECONDS_TO_RUN=20  # Number of seconds to run
END_TIME=$((SECONDS_TO_RUN + $(date +%s)))

for i in {0..0};
do
	./gradlew runIndif -q --console=plain  > outp.tmp &
	sleep 3
#	PID_JAS=$(jps | grep RunLocalMAS | awk 'NR==1{print $1}')
#	ros2 topic pub /ariac/start_human std_msgs/msg/Bool '{data: true}' --once 
	while [ $(date +%s) -lt $END_TIME ]; do 
	    # Do something while Jason is running (the file doesn't exist)
	    ps -p $PID_JAS -o %cpu 
	    sleep 1
	done
#	ros2 topic pub /ariac_human/go_home std_msgs/msg/Bool '{data: true}' --once 
	touch .stop___MAS
done
#python3 test.py 0
