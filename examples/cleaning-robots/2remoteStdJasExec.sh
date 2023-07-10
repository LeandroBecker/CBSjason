#!/bin/bash
echo "Running Standard REMOTE Jason "
# Check if the parameter is provided
if [ $# -ne 1 ]; then
  echo "at least one <integer> param is required"
  exit 1
fi
./gradlew clean
clear
echo "Running Standard REMOTE Jason "
# Store the input parameter
count=$1
count=$((count - 1))

for ((k=0; k<=0; k++)) #k<=5 for complete test
do
	runId="ls$k"
	cp runs/$runId ./marsPrjStd.mas2j
	runId="rs$k"
	echo "  "
	echo "Begin '$runId' "
	for ((i=0; i<=count; i++))
	do
		echo "RUN $runId - $i/$count"
		start_time=$(date +%s)
		./gradlew run -q --console=plain  > /dev/null #> $runId-$i.tmp   
#		jason marsPrjCritical.mas2j > $runId-$i.tmp 
		end_time=$(date +%s)
		elapsed_time=$((end_time - start_time))
		echo "Elapsed time: $elapsed_time s"
		echo " "
#		more reacTimes.log
		mv reacTimes.log $runId-$i.reacTimes.log.txt
		mv mas-0.log mas-0.log.$i
		echo " "
		echo 1 | sudo tee /proc/sys/vm/drop_caches > /dev/null
		echo 3 | sudo tee /proc/sys/vm/drop_caches > /dev/null
	done
#	python3 parseStdJ.py $count > $runId.log.txt
#	more $runId.log.txt
#	tar czvf $runId.tar.gz mas-* 
	rm mas-*
	echo "End '$runId' "
done
mv rs* results
echo "Experiment FINISHED"
