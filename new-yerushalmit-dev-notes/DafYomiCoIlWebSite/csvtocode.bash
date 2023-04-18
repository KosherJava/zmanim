outname="`echo $1 | cut -d. -f1 `".py
(
echo "$2  = ["
cat $1 |sed 's/"//g'  |  sed "s/[0-9]*$/,&/g"  | sed 's/,/","/g' | sed 's/^/["/g' | sed 's/$/"],/g'
echo "]"
) > $outname
