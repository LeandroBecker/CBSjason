import subprocess
import re
import time

# Define process name or ID to monitor
process = 'python3'

# Define regular expressions to extract CPU and memory utilization information
cpu_pattern = r'%Cpu\(s\):\s+(\d+\.\d+)'
mem_pattern = r'MiB Mem :\s+(\d+) total,\s+(\d+) free,\s+(\d+) used' 

# Initialize lists to store CPU and memory utilization values
cpu_utilization = []
mem_utilization = []

# Log CPU and memory utilization for process for 30 seconds
for i in range(100):
    # Run top command to get CPU and memory utilization of process
    #output = subprocess.check_output(['top', '-b', '-n', '1', '-p', '$(pgrep '+process+')']).decode('utf-8')
    output = subprocess.check_output("top -b -n 1 -p $(pgrep java | tr '\\n' ',' | sed 's/.$//')", shell=True).decode('utf-8')
    
    if not output:
    	break
    
    # Extract CPU utilization using regular expression
    cpu_match = re.search(cpu_pattern, output)
    if cpu_match:
        cpu_utilization.append(float(cpu_match.group(1)))
    
    # Extract memory utilization using regular expression
    mem_match = re.search(mem_pattern, output)
    if mem_match:
        mem_used = int(mem_match.group(3)) // 1024  # Convert to MB
        mem_total = int(mem_match.group(1)) // 1024  # Convert to MB
        mem_utilization.append(mem_used / mem_total * 100)
    
    # Wait for 1 second
    time.sleep(5)

# Calculate average CPU and memory utilization
avg_cpu_utilization = sum(cpu_utilization) / len(cpu_utilization)
avg_mem_utilization = sum(mem_utilization) / len(mem_utilization)

# Print average CPU and memory utilization
print(f'Average CPU utilization: {avg_cpu_utilization:.2f}%')
print(f'Average memory utilization: {avg_mem_utilization:.2f}%')

