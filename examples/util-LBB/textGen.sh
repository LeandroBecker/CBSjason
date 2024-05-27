#!/bin/bash
clear
# Check if the parameter is provided
if [ $# -ne 1 ]; then
  echo "at least one <integer> param is required"
  exit 1
fi
# Store the input parameter
count=$1
#count=$((count - 1))

for ((k=1; k<count; k++)) 
do
	runId="lc$k"
#	echo "+cr$k PER(_) : belA & belB <- critReac($k)."
	echo "+cb$k [cr]: belA & belB <- critReac($k)."
done
 
