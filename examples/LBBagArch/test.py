from bs4 import BeautifulSoup
import sys

if len(sys.argv)>1:
	fname = sys.argv[1]
else:
	fname = 'mas-0.xml'
# Reading the data inside the xml
# file to a variable under the name
# data
with open(fname, 'r') as f:
	data = f.read()

# Passing the stored data inside
# the beautifulsoup parser, storing
# the returned object
Bs_data = BeautifulSoup(data, "xml")

# Finding all instances of tag
# `unique`
#b_class = Bs_data.find_all('class', limit=2)

#rate for special measure
rsM = 1

#s->sum; c->counter; m7->multiple-of-7
cycleNum = 0
sPB = 0.0
cPB = 0
sm7PB = 0.0
cm7PB = 0
sumSense = 0.0
ctdSense = 0
sumSenseLBB = 0.0
ctdSenseLBB = 0
sm7Sense = 0.0
cm7Sense = 0
sDel = 0.0
cDel = 0
sAct = 0.0
cAct = 0
sm7Act = 0.0
cm7Act = 0
sumRC = 0.0
ctdRC = 0
sm7RC = 0.0
cm7RC = 0
lastTS = 0  #lastTimeStamp
sFaz = 0.0
cFaz = 0
lFaz = 0
sE2E = 0.0
cE2E = 0

#re.compile("^b")):
#' '.join(mystring.split())
#, limit=100): 
for tag in Bs_data.find_all('message'): 
    #print(tag.text)
    result = tag.text
    mycollapsedstring = result.split(' ') 
    if "Start" in mycollapsedstring:
    	cycleNum = int(mycollapsedstring[2])
    elif "LBB" in mycollapsedstring:
    	#if(mycollapsedstring[0] == 'LBB')
    	#print(tag.text)
    	lastTS = int(mycollapsedstring[5])
    	if "perceive+buf" in mycollapsedstring:
    		cPB=cPB+1
    		sPB = sPB + lastTS
    		if (cycleNum % rsM) == 0:
    			cm7PB = cm7PB + 1
    			sm7PB = sm7PB + lastTS
    	elif "senseLBB" in mycollapsedstring:
    		ctdSenseLBB=ctdSenseLBB+1
    		sumSenseLBB = sumSenseLBB + lastTS
    	elif "sense" in mycollapsedstring:
    		ctdSense=ctdSense+1
    		sumSense = sumSense + lastTS
    		if (cycleNum % rsM) == 0:
    			cm7Sense = cm7Sense + 1
    			sm7Sense = sm7Sense + lastTS
    	elif "delib" in mycollapsedstring:
    		cDel=cDel+1
    		sDel = sDel + lastTS
    	elif "act" in mycollapsedstring:
    		cAct=cAct+1
    		sAct = sAct + lastTS
    		if (cycleNum % rsM) == 0:
    			cm7Act = cm7Act + 1
    			sm7Act = sm7Act + lastTS
    	elif "resCycle" in mycollapsedstring:
    		ctdRC=ctdRC+1
    		sumRC = sumRC + lastTS
    		if (cycleNum % rsM) == 0:
    			cm7RC = cm7RC + 1
    			sm7RC = sm7RC + lastTS
    	elif "fazAction" in mycollapsedstring:
    		cFaz=cFaz+1
    		if(cFaz>1):
    			sFaz = sFaz + (lastTS-lFaz)
#    			print("%2d, Faz: %d"  % (cFaz, lastTS-lFaz))
    		lFaz=lastTS
    	elif "e2eAction" in mycollapsedstring:
    		cE2E=cE2E+1
    		if(cE2E>1):
    			sE2E=sE2E+lastTS
#    	elif "manualAction" in mycollapsedstring:
#    		cE2E=cE2E+1
#    		sE2E=sE2E+lastTS

print("RCs: %4d" % (cycleNum))
print("E2E: %4d" % (cE2E))
if(cPB>0):
	print("Avg   P+B: %12.0f"  % (sPB/cPB))
#print("Avg m7P+B: %12.0f"  % (sm7PB/cm7PB))
if(ctdSenseLBB>0):
	print("Avg SeLBB: %12.0f"  % (sumSenseLBB/ctdSenseLBB)) 
if(ctdSense>0):
	print("Avg Sense: %12.0f"  % (sumSense/ctdSense))
#print("Avg m7Sen: %12.0f"  % (sm7Sense/cm7Sense))
if(cDel>0):
	print("Avg   Del: %12.0f"  % (sDel/cDel))
if(cAct>0):
	print("Avg   Act: %12.0f"  % (sAct/cAct))
#print("Avg m7Act: %12.0f"  % (sm7Act/cm7Act))
if(ctdRC>0):
	print("Avg    RC: %12.0f"  % (sumRC/ctdRC))
#print("Avg  m7RC: %12.0f"  % (sm7RC/cm7RC))
#print("Avg   Faz: %12.0f"  % (sFaz/(cFaz-1)))
if(cE2E>0):
	print("Avg   E2E: %12.0f"  % (sE2E/(cE2E)))
#print("%3d Faz: every %4.2f RC"      % (cFaz,cycleNum/cFaz))
    
    
#    result = third_child.text



# Using find() to extract attributes
# of the first instance of the tag
#b_name = Bs_data.find('child', {'name':'Frank'})

#print(b_name)

# Extracting the data stored in a
# specific attribute of the
# `child` tag
#value = b_name.get('test')

#print(value)

