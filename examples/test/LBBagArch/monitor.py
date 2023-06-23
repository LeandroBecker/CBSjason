import subprocess
import re

# Define process name or ID to monitor
process = "python3 | tr '\\n' ',' | sed 's/.$//'"

# Run top command to get CPU utilization of python processes
# print(subprocess.check_output("pgrep python3 | tr '\\n' ',' | sed 's/.$//'", shell=True))

output = subprocess.check_output("top -b -n 1 -p $(pgrep python3 | tr '\\n' ',' | sed 's/.$//')", shell=True).decode('utf-8')

# with open('lbb.log', 'r') as f:
# 	output = f.read()
	
# Extract CPU utilization information using regular expression
pattern = r'python3\s+\d+\s+\S+\s+\S+\s+\S+\s+\S+\s+\S+\s+\S+\s+\S+\s+(\d+\.\d+)'
matches = re.findall(pattern, output)

# Calculate average CPU utilization
cpu_utilization = sum([float(match) for match in matches]) / len(matches)

# Print average CPU utilization
print(f'Average CPU utilization of python processes: {cpu_utilization:.2f}%')


