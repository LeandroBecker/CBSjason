#!/bin/bash
echo "Starting experiments script"
source catkin_ws/devel/setup.bash 
cp src/java/MyRosMasterStdJas.jaBAA src/java/MyRosMaster.java
cp src/java/RosEnv.jaBAA src/java/RosEnv.java
cp src/agt/sample_agent_M.Std src/agt/sample_agent_M.yaml
cp build.gradle.maiquel.stdJason.txt build.gradle
#pwd 
#cp ../ariac_human/human_control.py .
SECONDS_TO_RUN=30  # Number of seconds to run

END_TIME=$((SECONDS_TO_RUN + $(date +%s)))

for i in {0..0};
do
	./gradlew run -q --console=plain  > outpGradlew.tmp &
	sleep 3
	PID_JAS=$(jps | grep RunLocalMAS | awk 'NR==1{print $1}')
	rosrun my_ros_package example_node.py > outpROS.tmp &
	#ros2 topic pub /ariac/start_human std_msgs/msg/Bool '{data: true}' --once 
	while [ $(date +%s) -lt $END_TIME ]; do 
	    # Do something while Jason is running (the file doesn't exist)
	    #ps -p $PID_JAS -o %cpu 
	    sleep 2
	done
	rostopic pub finish std_msgs/Bool '{data: true}' --once 
	touch .stop___MAS
done
rm src/java/MyRosMaster.java
rm src/java/RosEnv.java
echo "Finishing experiments script"