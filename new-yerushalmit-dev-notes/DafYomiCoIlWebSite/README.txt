
README.txt - this file 

I generated csv file by cutting and pasting the html 
tables from the following sites to spreadsheet and saving as csv files

Yerushalmi Schedule Original  11th cycle
OrigDafYerushalmiCycle11.csv: from: 
https://www.dafyomi.co.il/calendars/calendaryeru.htm 


Yerushalmi Schedule Oz v'Hadar 11th cycle
OzVHadarDafYerushalmiCycle11.csv: from:
https://www.dafyomi.co.il/calendars/calendaryeruoz.htm


csvtocode.bash  - generate python table from csv file using sed and echo
mkdtables.bash  - build our 2 python tables  from csv
OrigDafYerushalmiCycle11.py - python table  generated from csv
OzVHadarDafYerushalmiCycle11.py - python table  generated from csv


mkjavacode.bash      -- run to generate java snippets to cut and paset into the code

gencounttableorig.py -- generate java snippets of original blatt counts
gencounttable.py     -- generate java snippets of new blatt counts
gennamellookup.py    -- generate table  to map new mescta to old
gennametables.py     --  generate tables of mesechta names only
kznames.py           --  names from java code
namecheck.py         --  confirm name tables are good
nametables.py        --  will be used to generate Java test code table

Counts.java     --  java snippet for blatt per mesecta counts
OrigCounts.java --  java snippet for original counts as validataion 
Map.java        -- map new mesechta number to old (to use same name tables)
