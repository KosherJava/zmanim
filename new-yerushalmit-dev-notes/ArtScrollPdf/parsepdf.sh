
pdftohtml -s -i yerushalmi-yomi-calendar_download_new.pdf  2> /dev/null > /dev/null
cat yerushalmi-yomi-calendar_download_new-html.html |
grep "<p"    | 
egrep -v "DAF *YOMI *YERUSHALMI *CALENDAR" |
egrep -v "FOLLOWING *THE *SCHOTTENSTEIN *EDITION *TALMUD *YERUSHALMI"  | 
	sed "s/\o342\o226\o240/XXX/g" |
	sed 's/ *JANUARY /XXX\nYEAR-/g' |
	sed 's/ *FEBRUARY /XXX\nYEAR-/g' |
	sed 's/ *MARCH /XXX\nYEAR-/g' |
	sed 's/ *APRIL /XXX\nYEAR-/g' |
	sed 's/ *MAY /XXX\nYEAR-/g' |
	sed 's/ *JUNE /XXX\nYEAR-/g' |
	sed 's/ *JULY /XXX\nYEAR-/g' |
	sed 's/ *AUGUST /XXX\nYEAR-/g' |
	sed 's/ *SEPTEMBER /XXX\nYEAR-/g' |
	sed 's/ *OCTOBER /XXX\nYEAR-/g' |
	sed 's/ *NOVEMBER /XXX\nYEAR-/g' |
	sed 's/ *DECEMBER /XXX\nYEAR-/g' | 
	sed 's/&#160;//g' |
  sed "s/<p[^>]*>//g"  |
 sed "s/<\/p>//" > parse.out 2> /dev/null
 python clean.py | tr -d '[' | tr -d ']'  > yerushalmi.csv
