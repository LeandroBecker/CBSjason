Guide for howto use ROS-A with an E-MAS Project

1) Create the class to represent the Environment, which in this example I will call RosEnv.java (see src/java).

2) It must be informed the existence of such environment in the .mas2j file. In this example this is illustrated in the project agCrit.mas2j, as follows:
MAS teste_embedded_mas {
    infrastructure: Local
    environment: RosEnv
    agents: uav1 agentClass embedded.mas.bridges.jacamo.CyberPhysicalAgent agentArchClass embedded.mas.bridges.jacamo.DemoEmbeddedAgentArch;
}

3) The agent code must invoke the external-action "critReac0" instead of calling the internal-action required by E-MAS.
In the Critical Jason (CB2A), "critReac0" is the default operation (no code is needed).
For the Standard Jason, calling such ext-action must be provided by the programmer. 



