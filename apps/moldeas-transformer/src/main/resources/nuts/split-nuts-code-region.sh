#!/bin/bash

rm -f *.tmp
rm -f *item*.txt
cat nuts-code-region.txt > nuts-code-region.tmp

#Bucle
headValue=90
lines=1
i=0
while [  $lines -gt 0 ]; do
	echo "Creating file nuts-code-region-item-${i}.txt "
	`head -$headValue nuts-code-region.tmp > nuts-code-region-item-${i}.txt`
	let lines=`wc -l nuts-code-region.tmp | cut --delimiter=" " -f1`-${headValue}
	let i=$i+1
	`tail -${lines} nuts-code-region.tmp > nuts-code-region.tmp2`
	cat nuts-code-region.tmp2 > nuts-code-region.tmp
done
rm -f *.tmp

#Check cat `ls  *item*.txt `| wc -l = cat nuts-code-region.txt | wc -l


