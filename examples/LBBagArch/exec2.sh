#!/bin/bash
echo "Modified Jason (NO Env) in : "
pwd 
for i in {0..10};
do
    jason lbb3.mas2j > outp2
done
python3 test.py 9

