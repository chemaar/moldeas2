#!/bin/bash
#echo "Processing " "$1" "---" "$2"
awk 'NR==FNR{a[$0];next} $0 in a' $1 $2 > "$1-$2-tp"
TP=` cat "$1-$2-tp" | wc -l`
FP=`awk 'NR==FNR { a[$0]; next } !($0 in a)' "$1" "$2" | wc -l`
FN=`awk 'NR==FNR { a[$0]; next } $0 in a { delete a[$0]; next } 1; END { for (b in a) print b }' "$1" "$1-$2-tp" | wc -l`
echo "TP, FP, FN"
echo $TP "," $FP "," $FN
rm -f "$1-$2-tp"

