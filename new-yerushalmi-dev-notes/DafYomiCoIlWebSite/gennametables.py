
import OrigDafYerushalmiCycle11
origtab = OrigDafYerushalmiCycle11.table

import OzVHadarDafYerushalmiCycle11
oztab = OzVHadarDafYerushalmiCycle11.table

lastm = ""

print("orignames = [")
for x in origtab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		if mesechta != lastm:
			print ('"' + mesechta  + '",')
		lastm = mesechta

print("]\n")


print("oznames = [")
for x in oztab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		if mesechta != lastm:
			print ('"'+ mesechta+ '",')
		lastm = mesechta

print("]\n")

