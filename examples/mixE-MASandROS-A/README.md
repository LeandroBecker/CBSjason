# lbbROS1examples
In theory it can be added to any folder, but I will configure it to be in $HOME/embedded-mas/examples

## Running the example

1. Make sure that ROS is properly sourced. In my instalation simply run the following bash alias: 
```
noetic
```

2. Source the ROS workspace, for example: 
```
source ~/catkin_ws/devel/setup.bash

BTW: when making a new installation, first run catkin_make clean at ~/catkin_ws
```

3. Start the roscore:
```
roscore
```

4. Launch the bridge between ROS and Java
```
roslaunch rosbridge_server rosbridge_websocket.launch
```

5. Finally, execute the application
```
There are some scripts available that facilitate launching the agent:
exec110sMaiquelCritcalJason
exec110sMaiquelStdJason
exec110sRafaelCritcalJason
exec110sRafaelStdJason

```
