import nametables



print ("private final static int[] NewMessechtaMap = {", end = "")
for s  in nametables.oznames:
	print( nametables.orignames.index(s), ', ', end='')

print("};");





