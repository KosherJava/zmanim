

import OzVHadarDafYerushalmiCycle11

oztab = OzVHadarDafYerushalmiCycle11.table

from dateutil import parser

for x in oztab:
	if len(x[5].strip()) > 0:
		mesechta =x[4].strip()
		page = int(x[5].strip())
		md = x[0].strip()
		year = x[1].strip()
		ndy = md + " " + year
		d = parser.parse(ndy).strftime("%Y-%m-%d")
		out = '{ "' + d + '", "' +  mesechta + '", "' + '"' + str(page) + '"},'
		print (out )


