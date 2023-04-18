#!/usr/bin/env python

import rospy
from std_msgs.msg import String
from std_msgs.msg import Bool
from std_msgs.msg import Int32


class ExampleNode:
    def __init__(self):
        # Initialize node
        rospy.init_node('example_node')

        # Create publisher
        self.pubTime = rospy.Publisher('current_time', String, queue_size=10)
        self.pubVal = rospy.Publisher('value1', String, queue_size=10)

        # Create subscriber
        rospy.Subscriber('/current_time', String, self.callback)

    def run(self):
        rate = rospy.Rate(1) # 10 Hz
        while not rospy.is_shutdown():
            # Publish message
            message = String()
            message.data = "unknown"
            self.publisher.publish(message)

            rate.sleep()

    def callback(self, message):
        # Print received message
        rospy.loginfo("Received message: %s", message.data)

if __name__ == '__main__':
    node = ExampleNode()
    node.run()
