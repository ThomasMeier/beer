# beer

A Clojure application designed to work with a Raspberry Pi. It expects two
temperature sensors, two pumps, and two relays.

## Installation on the Pi

1. Set up environment
2. Build jar
3. `rsync` jar
4. Run jar
5. Port forward nginx and restart

### 1. Environment

Must set these variables in the Pi. Add to `/etc/environment`. Example:

```
# Path to sensor file 1
TEMP_1=/sys/bus/drivers/w1/28-03135/w1_slave
# Path to sensor file 2
TEMP_2=/sys/bus/drivers/w1/28-09877/w1_slave
```

### 2. Build Jar

Pull latest from this repo, use leiningen.

```
git [clone|pull] git@github.com:ThomasMeier/beer.git
cd beer
lein ring uberjar
```

### 3. rsync jar

Upload jar to pi.

```
rsync target/*standalone.jar pi:~/beer.jar
```

### 4. Run jar

Log into pi and run the jar (Pi will need a few to start it)

```
ssh pi
java -jar beer.jar
```

(If the pi is dedicated to brewing, and no version changes have happened,
  this can be automated on start up for the pi with upstart or whatever. That
  way it'll start up when you plug the pi in.)

### 5. Config nginx (once)

You should only need to configure nginx once.

```
sudo vim /etc/nginx/sites-available/default
```

Set `proxy_pass` to localhost on port 3000. This only needs to do on a fresh
installation of nginx.

### 6. Restart nginx (always thereafter)

```
sudo service nginx restart
```

### 7. Verify server is working

In browser, go to IP address on LAN assigned to the pi.

***

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build

```
lein clean
lein cljsbuild once min
```

Copyright 2016 Thomas Meier
