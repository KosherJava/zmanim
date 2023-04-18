f = open("parse.out", "r")

l  = f.readline() 
x = []
year="2023"
x.append(year)
while l:
	if  "XXX" in l:
		if len(x) > 1:
			print (x)
		x = []
		x.append(year)
	else:
		if l.startswith("YEAR-"):
			year =  l.split("-")[1].strip()
		else: 
			x.extend(l.strip().split(" "))
	l = f.readline()
		
f.close()
print(x)



