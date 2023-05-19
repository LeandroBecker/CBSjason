#!/bin/bash
clear
# Check if the parameter is provided
if [ $# -ne 1 ]; then
  echo "at least one <integer> param is required"
  exit 1
fi
# Store the input parameter
count=$1
count=$((count - 1))

for ((k=1; k<=3; k++))
do
	runId="lc$k"
	cp runs/$runId ./marsPrjCritical.mas2j
	echo "  "
	echo "Begin '$runId' "
	for ((i=0; i<=count; i++))
	do
		echo "RUN $runId - $i/$count"
		start_time=$(date +%s)
	#	./gradlew runIndif -q --console=plain  > outp.tmp  
		jason marsPrjCritical.mas2j > $runId-$i.tmp 
		end_time=$(date +%s)
		elapsed_time=$((end_time - start_time))
		echo "Elapsed time: $elapsed_time s"
		echo " "
#		more reacTimes.log
		mv reacTimes.log $runId-$i.reacTimes.log.txt
		mv mas-0.log mas-0.log.$i
		echo " "
		sleep 5
	done
	python3 test.py $count > $runId.log.txt
	more $runId.log.txt
	tar czvf $runId.tar.gz mas-* 
	rm mas-*
	echo "End '$runId' "
done
mv lc* results
echo "Experiment FINISHED"
