#!/bin/bash

# Ask user for the process name
read -p "Enter the name of the process: " proc_name

# Get the PIDs of the processes using pgrep command
pids=$(pgrep "$proc_name")

# Check if at least two PIDs were found
if [ $(echo "$pids" | wc -w) -lt 2 ]; then
  echo "Error: Found $(echo "$pids" | wc -w) PIDs for process name '$proc_name'. Expected at least 2."
  exit 1
fi

# Initialize variables
cpu_sum=()
count=()

# Function to calculate the average CPU usage of a process
function calculate_avg_cpu {
  pid=$1
  cpu_sum=$2
  count=$3
  avg_cpu=$(echo "scale=2; $cpu_sum / $count" | bc)
  echo "Average CPU usage of $proc_name with PID $pid: $avg_cpu%"
}

# Loop to get the CPU usage of the processes until a key is pressed
while true; do
  # Initialize variables for the CPU usage sums and count
  cpu_sum=()
  count=()

  # Loop through each PID and get the CPU usage using top command
  for pid in $pids; do
    # Get the CPU usage of the process using top command
    # cpu_usage=$(top -bn1 -p "$pid" | awk '/^%Cpu/{getline;print}')
    cpu_usage=$(top -bn1 -p "$pid" | awk '/^%Cpu/ {for (i=2;i<=NF;i++) printf "%s ", $i}')
  
    # Split the CPU usage output into a variable
    cpu=$(echo $cpu_usage | awk '{print $1}')

    # Add the CPU usage and count to the arrays
    cpu_sum+=($cpu)
    count+=("1")
  
    # Print the current CPU usage of the process
    echo "Current CPU usage of $proc_name with PID $pid: $cpu%"
  done

  # Calculate and print the average CPU usage for each process
  for i in "${!pids[@]}"; do
    calculate_avg_cpu "${pids[$i]}" "${cpu_sum[$i]}" "${count[$i]}"
  done

# Wait for user input
  read -n1 -t 1 -r -p "Press any key to continue or 'q' to quit... " key
  echo ""
  if [[ "$key" == "q" ]]; then
    break
  fi
done

echo "Script ended."

#%Cpu(s): 10.9 us,  1.1 sy,  0.0 ni, 87.7 id,  0.0 wa,  0.0 hi,  0.3 si,  0.0 s | awk '{print $1}'

