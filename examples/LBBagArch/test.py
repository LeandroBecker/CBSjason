from bs4 import BeautifulSoup
import sys

if len(sys.argv)>1:
	numRounds = sys.argv[1]
nr = int(numRounds) + 1

#s->sum; c->counter;
cycleNum = [0] * nr
#sPB = [0] * nr
#cPB = [0] * nr
sumSense = [0] * nr
#ctdSense = [0] * nr
sumSenseLBB = [0] * nr
#ctdSenseLBB = [0] * nr
ctdRCs = [0] * nr
sDel = [0] * nr
#cDel = [0] * nr
sAct = [0] * nr
#cAct = [0] * nr
sumRC = [0] * nr
#ctdRC = [0] * nr
lastTS = [0] * nr  #lastTimeStamp
sFaz = [0] * nr
cFaz = [0] * nr
lFaz = [0] * nr
sE2E = [0] * nr
cE2E = [0] * nr
cRePl= [0] * nr
sRePl= [0] * nr
sApPl = [0] * nr
sSeOp = [0] * nr
sESL = [0] * nr
cLPer = [0] * nr
sLPer = [0] * nr

#print  (cycleNum)

#Read/parse more than one log file: 
for i in range(0, nr):
	# Reading the data inside the xml file
	fname = 'mas-0.log.'+str(i)
	#print(fname)
	with open(fname, 'r') as f:
		data = f.read()

	# Passing the stored data inside the beautifulsoup parser
	Bs_data = BeautifulSoup(data, "xml")

	for tag in Bs_data.find_all('message'): 
	    #print(tag.text)
	    result = tag.text
	    mycollapsedstring = result.split(' ') 
	    if "Start" in mycollapsedstring:
	    	cycleNum[i] = int(mycollapsedstring[2])
	    elif "LBB" in mycollapsedstring:
	    	#if(mycollapsedstring[0] == 'LBB')
	    	#print(tag.text)
	    	lastTS[i] = int(mycollapsedstring[5])
	    	#time1 = int(mycollapsedstring[5])
	    	#if "perceive+buf" in mycollapsedstring:
	    	#	cPB[i]=cPB[i]+1
	    	#	sPB[i] = sPB[i] + lastTS[i]
	    	#if "criticalRC" in mycollapsedstring:
	    	#	ctdRCs[i]=ctdRCs[i]+1
	    	#	sumSenseLBB[i] = sumSenseLBB[i] + time1	
	    	#	sumSense[i] = sumSense[i] + time2
	    	#	sDel[i] = sDel[i] + time3
	    	#	sAct[i] = sAct[i] + time4
	    	#	sumRC[i] = sumRC[i] + time5
	    	if "criticalRC" in mycollapsedstring:
	    		ctdRCs[i]=ctdRCs[i]+1
	    		sumSenseLBB[i] = sumSenseLBB[i] + lastTS[i]
	    	#	ctdSenseLBB[i]=ctdSenseLBB[i]+1
	    	elif "sense" in mycollapsedstring:
	    		sumSense[i] = sumSense[i] + lastTS[i]
	    	#	ctdSense[i]=ctdSense[i]+1
	    	elif "delib" in mycollapsedstring:
	    		sDel[i] = sDel[i] + lastTS[i]
	    	#	cDel[i]=cDel[i]+1
	    	elif "act" in mycollapsedstring:
	    		sAct[i] = sAct[i] + lastTS[i]
	    	#	cAct[i]=cAct[i]+1
	    	elif "resCycle" in mycollapsedstring:
	    		sumRC[i] = sumRC[i] + lastTS[i]
	    	#	ctdRC[i]=ctdRC[i]+1
	    	elif "lbbPercept" in mycollapsedstring:
		    	time1 = int(mycollapsedstring[5])
		    	time2 = int(mycollapsedstring[6])
		    	time3 = int(mycollapsedstring[7])
		    	time4 = int(mycollapsedstring[8])
		    	time5 = int(mycollapsedstring[9])
	    		cLPer[i]=cLPer[i]+1
	    		sLPer[i] = sLPer[i] + time1
	    		sRePl[i] = sRePl[i] + time2
	    		sApPl[i] = sApPl[i] + time3
	    		sSeOp[i] = sSeOp[i] + time4
	    		sESL[i] = sESL[i] + time5
	    	#elif "relevantPlans" in mycollapsedstring:
	    	#	cRePl[i]=cRePl[i]+1
	    	#	sRePl[i] = sRePl[i] + lastTS[i]
	    	#elif "applicablePlans" in mycollapsedstring:
	    	#	sApPl[i] = sApPl[i] + lastTS[i]
	    	#elif "selectOption" in mycollapsedstring:
	    	#	sSeOp[i] = sSeOp[i] + lastTS[i]
	    	#elif "endSenLBB" in mycollapsedstring:
	    	#	sESL[i] = sESL[i] + lastTS[i]
	    	elif "fazAction" in mycollapsedstring:
	    		cFaz[i]=cFaz[i]+1
	    		if(cFaz[i]>1):
	    			sFaz[i] = sFaz[i] + (lastTS[i]-lFaz[i])
	    		lFaz[i]=lastTS[i]
	    	elif "e2eAction" in mycollapsedstring:
	    		cE2E[i]=cE2E[i]+1
	    		if(cE2E[i]>1):
	    			sE2E[i]=sE2E[i]+lastTS[i]
	#    	elif "manualAction" in mycollapsedstring:
	#    		cE2E[i]=cE2E[i]+1
	#    		sE2E[i]=sE2E[i]+lastTS[i]
#end for

#cycleNum - number of cycles 
avgCN = 0
print("RCs: " , end='')
print  (cycleNum)
for i in range(0, nr):
	avgCN = avgCN + cycleNum[i]

#cE2E - quantity of observed external functions
avgcE2E = 0
print("E2E: " , end='')
print  (cE2E)
for i in range(0, nr):
	avgcE2E = avgcE2E + cE2E[i]

#avgPB = 0
#for i in range(0, nr):
#	avgPB = avgPB + (sPB[i]/cPB[i])

avgSLB = 0
for i in range(0, nr):
	avgSLB = avgSLB + (sumSenseLBB[i]/ctdRCs[i])

avgSen = 0
for i in range(0, nr):
	avgSen = avgSen + (sumSense[i]/ctdRCs[i])
#	avgSen = avgSen + (sumSense[i]/ctdSense[i])

avgDel = 0
for i in range(0, nr):
	avgDel = avgDel + (sDel[i]/ctdRCs[i])
#	avgDel = avgDel + (sDel[i]/cDel[i])

avgAct = 0
for i in range(0, nr):
	avgAct = avgAct + (sAct[i]/ctdRCs[i])
#	avgAct = avgAct + (sAct[i]/cAct[i])

avgRC = 0
for i in range(0, nr):
	avgRC = avgRC + (sumRC[i]/ctdRCs[i])
#	avgRC = avgRC + (sumRC[i]/ctdRC[i])

avgLBP = 0
for i in range(0, nr):
	avgLBP = avgLBP + (sLPer[i]/cLPer[i])

avgRP = 0
for i in range(0, nr):
	avgRP = avgRP + (sRePl[i]/cLPer[i]) 

avgAP = 0
for i in range(0, nr):
	avgAP = avgAP + (sApPl[i]/cLPer[i])

avgSO = 0
for i in range(0, nr):
	avgSO = avgSO + (sSeOp[i]/cLPer[i])

avgESL = 0
for i in range(0, nr):
	avgESL = avgESL + (sESL[i]/cLPer[i])

#Print all average numbers
print("Avg RCs: %4d " % (avgCN/nr))
#print("Avg E2E: %4d " % (avgcE2E/nr))
#print("Avg   P+B: %12.0f"  % avgPB)
print("Avg Sense: %12.0f"  % avgSen)
print("Avg   Del: %12.0f"  % avgDel)
print("Avg   Act: %12.0f"  % avgAct)
print("Avg    RC: %12.0f"  % avgRC) 
print("Avg  CB2E: %12.0f"  % avgSLB, end='') 
print(" - %2.2f of RC" % (avgSLB/avgRC))
print("Avg  CPer: %12.0f"  % avgLBP) 
print("Avg  RePl: %12.0f"  % avgRP, end='')  
print(" - %2.2f of CB2E" % (avgRP/avgSLB))
print("Avg  ApPl: %12.0f"  % avgAP) 
print("Avg  SeOp: %12.0f"  % avgSO) 
print("Avg  ExAc: %12.0f"  % avgESL) 

#if(avgcE2E > 0):
#	avgsE2E = 0
#	for i in range(0, nr):
#		avgsE2E = avgsE2E + (sE2E[i]/cE2E[i])
#	print("Avg   E2E: %12.0f"  % avgsE2E)

#####################################################
## REPEAT all from 5..9 (change from l.305)
#####################################################

