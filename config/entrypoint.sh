#!/bin/sh


a=0

while [ $a -lt 10 ]
do
   echo $a
   a=`expr $a + 1`
   sleep 1
done

exit 0