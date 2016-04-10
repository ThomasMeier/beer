#/bin/sh
java -jar /home/pi/beer.jar 2>&1 beer.log &
service nginx restart
