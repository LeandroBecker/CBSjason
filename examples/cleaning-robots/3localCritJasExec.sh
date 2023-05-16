#!/bin/bash
echo "Running Standard REMOTE Jason "
cp build-gradle.rafael.standardLocalJason.txt build.gradle 
./gradlew clean
clear

for i in {0..0};
do
	echo "RUN number $i "
	start_time=$(date +%s)
#	./gradlew runIndif -q --console=plain  > outp.tmp  
	jason marsPrjCritical.mas2j > outp
	end_time=$(date +%s)
	elapsed_time=$((end_time - start_time))
	echo "Elapsed time: $elapsed_time s"
	echo " "
	more reacTimes.log
	mv mas-0.log mas-0.log.$i
	mv reacTimes.log reacTimes.log.$i
	echo " "
#	sleep 3
#	PID_JAS=$(jps | grep RunLocalMAS | awk 'NR==1{print $1}')
#	ros2 topic pub /ariac/start_human std_msgs/msg/Bool '{data: true}' --once 
#	while [ $(date +%s) -lt $END_TIME ]; do 
	    # Do something while Jason is running (the file doesn't exist)
#	    ps -p $PID_JAS -o %cpu 
#	    sleep 1
#	done
#	ros2 topic pub /ariac_human/go_home std_msgs/msg/Bool '{data: true}' --once 
#	touch .stop___MAS
done
#cp mas-0.log mas-0.log.0
python3 test.py 1 > summary.log.txt
more summary.log.txt


