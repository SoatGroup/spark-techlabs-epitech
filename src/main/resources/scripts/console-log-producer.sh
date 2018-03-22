#!/bin/bash
# nc localhost 9999 on another console to read the  published message

filename='logfile.log'
echo Start
while read p; do 
	echo $p
	sleep 0.1
done < $filename | nc -lk 9999
