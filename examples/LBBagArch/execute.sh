#!/bin/bash
echo "Running Jason in : "
pwd 
for i in {0..10};
do
    jason lbb2.mas2j > outp
done
python3 test.py 9

