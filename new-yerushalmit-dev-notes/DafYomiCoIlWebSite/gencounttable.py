
# import OrigDafYerushalmiCycle11
# tab = OrigDafYerushalmiCycle11.table

import OzVHadarDafYerushalmiCycle11
tab = OzVHadarDafYerushalmiCycle11.table

lastm = ""
lastp = 0
for x in tab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		page = int(x[5].strip())
		if page < lastp:
			print (lastp, "// ", lastm)
		lastm = mesechta
		lastp = page

print (lastp, "// ", lastm)
