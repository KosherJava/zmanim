import kznames
import nametables

for i,n  in enumerate(kznames.kznames):
	if nametables.orignames[i] != kznames.kznames[i]:
		print("--------diff:");
	print (i, "web", nametables.orignames[i],"kz",  kznames.kznames[i], kznames.kzhnames[i])  
