
# import OrigDafYerushalmiCycle11
# tab = OrigDafYerushalmiCycle11.table

import OzVHadarDafYerushalmiCycle11
oztab = OzVHadarDafYerushalmiCycle11.table

lastm = ""
lastp = 0
totpage = 0
print("private final static int[] New_BLATT_PER_MASSECTA = {");
for x in oztab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		page = int(x[5].strip())
		if page < lastp:
			totpage += lastp
			print (lastp, ", // ", lastm)
		lastm = mesechta
		lastp = page
		

print (lastp, "// ", lastm)
print("};")
totpage += lastp

print ("private final static int NEW_WHOLE_SHAS_DAFS = ", totpage ,";")

