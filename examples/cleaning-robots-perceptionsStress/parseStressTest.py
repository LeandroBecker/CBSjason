from bs4 import BeautifulSoup
import sys

if len(sys.argv)>1:
	numRounds = sys.argv[1]
	trial = sys.argv[2]
nr = int(numRounds) + 1

#s->sum; c->counter;
cycleNum = [0] * nr
#sPB = [0] * nr
#cPB = [0] * nr
sumSense = [0] * nr
ctdSense = [0] * nr
sumExpRP = [0] * nr
#ctdSenseLBB = [0] * nr
ctdRCs = [0] * nr
sDel = [0] * nr
cDel = [0] * nr
sAct = [0] * nr
cAct = [0] * nr
sumRC = [0] * nr
ctdRC = [0] * nr
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

erpMat = []
rcMat  = []

#print  (cycleNum)

#Read/parse more than one log file: 
for i in range(0, nr):
	# Reading the data inside the xml file
	fname = 'results/'+trial+'-mas-0.log.'+str(i)
	#print(fname)
	with open(fname, 'r') as f:
		data = f.read()

	# Passing the stored data inside the beautifulsoup parser
	Bs_data = BeautifulSoup(data, "xml")

	for tag in Bs_data.find_all('message'): 
	    rcRow  = []
	    erpRow = []
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
	    	#	sumExpRP[i] = sumExpRP[i] + time1	
	    	#	sumSense[i] = sumSense[i] + time2
	    	#	sDel[i] = sDel[i] + time3
	    	#	sAct[i] = sAct[i] + time4
	    	#	sumRC[i] = sumRC[i] + time5
	    	if "criticalRC" in mycollapsedstring:
	    		rcRow.append(lastTS[i])
	    		print("E%d, %d, %d," % (i, lastTS[i], ctdRCs[i]))
	    		ctdRCs[i]=ctdRCs[i]+1
	    		sumExpRP[i] = sumExpRP[i] + lastTS[i]
	    	# elif "sense" in mycollapsedstring:
	    	# 	sumSense[i] = sumSense[i] + lastTS[i]
	    	# 	ctdSense[i]=ctdSense[i]+1
	    	# elif "delib" in mycollapsedstring:
	    	# 	sDel[i] = sDel[i] + lastTS[i]
	    	# 	cDel[i]=cDel[i]+1
	    	# elif "act" in mycollapsedstring:
	    	# 	sAct[i] = sAct[i] + lastTS[i]
	    	# 	cAct[i]=cAct[i]+1
	    	elif "resCycle" in mycollapsedstring:
	    		sumRC[i] = sumRC[i] + lastTS[i]
	    		print("S%d, %d, %d," % (i, lastTS[i], ctdRC[i]))
	    		ctdRC[i]=ctdRC[i]+1
	    		erpRow.append(lastTS[i])
	    erpMat.append(erpRow)
	    #print(erpRow)
	    rcMat.append(rcRow)
	    #end for


#cycleNum - number of cycles 
# avgCN = 0
# print("RCs: " , end='')
# print  (cycleNum)
# for i in range(0, nr):
# 	avgCN = avgCN + cycleNum[i]

#cE2E - quantity of observed external functions
# avgcE2E = 0
# print("E2E: " , end='')
# print  (cE2E)
# for i in range(0, nr):
# 	avgcE2E = avgcE2E + cE2E[i]

#avgPB = 0
#for i in range(0, nr):
#	avgPB = avgPB + (sPB[i]/cPB[i])

avgERP = 0
#for i in range(0, nr):
#	avgERP = avgERP + (sumExpRP[i]/ctdRCs[i])

# avgSen = 0
# for i in range(0, nr):
# 	avgSen = avgSen + (sumSense[i]/ctdSense[i])

# avgDel = 0
# for i in range(0, nr):
# 	avgDel = avgDel + (sDel[i]/cDel[i])

# avgAct = 0
# for i in range(0, nr):
# 	avgAct = avgAct + (sAct[i]/cAct[i])

avgRC = 0
#for i in range(0, nr):
#	avgRC = avgRC + (sumRC[i]/ctdRC[i])

# avgLBP = 0
# for i in range(0, nr):
# 	avgLBP = avgLBP + (sLPer[i]/cLPer[i])

# avgRP = 0
# for i in range(0, nr):
# 	avgRP = avgRP + (sRePl[i]/cLPer[i]) 

# avgAP = 0
# for i in range(0, nr):
# 	avgAP = avgAP + (sApPl[i]/cLPer[i])

# avgSO = 0
# for i in range(0, nr):
# 	avgSO = avgSO + (sSeOp[i]/cLPer[i])

# avgESL = 0
# for i in range(0, nr):
# 	avgESL = avgESL + (sESL[i]/cLPer[i])

#print("nRC: %d"  % nr) 
#Print all average numbers
#print("Avg RCs: %4d " % (avgCN/nr))
#print("Avg E2E: %4d " % (avgcE2E/nr))
#print("Avg   P+B: %12.0f"  % avgPB)
# print("Avg Sense: %12.0f"  % avgSen)
# print("Avg   Del: %12.0f"  % avgDel)
# print("Avg   Act: %12.0f"  % avgAct)
#print("Avg ExpRP: %12.0f"  % (avgERP/nr)) 
#print("Avg    RC: %12.0f"  % (avgRC/nr)) 
##print("Avg RC/ExpRP: %f"  % ((avgRC/nr)/(avgERP/nr))) 
# tlog = avgERP - (avgLBP + avgRP + avgAP + avgSO + avgESL)
# avgERP = avgERP - tlog
# print("Avg  CB2E: %12.0f"  % avgERP, end='') 
# print(" - %2.2f of RC" % (avgERP/avgRC))
# print("Avg  CPer: %12.0f"  % avgLBP) 
# print("Avg  RePl: %12.0f"  % avgRP, end='')  
# print(" - %2.2f of CB2E" % (avgRP/avgERP))
# print("Avg  ApPl: %12.0f"  % avgAP, end='')   
# print(" - %2.2f of CB2E" % (avgAP/avgERP))
# print("Avg  SeOp: %12.0f"  % avgSO) 
# print("Avg  ExAc: %12.0f"  % avgESL) 

#print (erpMat)
#print  (sumRC)
#if(avgcE2E > 0):
#	avgsE2E = 0
#	for i in range(0, nr):
#		avgsE2E = avgsE2E + (sE2E[i]/cE2E[i])
#	print("Avg   E2E: %12.0f"  % avgsE2E)

#####################################################
## REPEAT all from 5..9 (change from l.305)
#####################################################

