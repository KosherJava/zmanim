import OrigDafYerushalmiCycle11
import kznames
import nametables

tab = OrigDafYerushalmiCycle11.table

from dateutil import parser

for x in tab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		kzname = kznames.kznames[nametables.orignames.index(mesechta)]
		page = int(x[5].strip())
		md = x[0].strip()
		year = x[1].strip()
		ndy = md + " " + year
		d = parser.parse(ndy).strftime("%Y-%m-%d")
		out = '{ "' + d + '", "' +  kzname + '", "' + '"' + str(page) + '"},'
		print (out )


