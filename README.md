# beer

A Clojure application designed to work with a Raspberry Pi. It expects two
temperature sensors, two pumps, and two relays.

![alt text](https://github.com/ThomasMeier/beer/blob/master/doc/beerui.png "Beer UI")

## Installation on the Pi

1. Clone this project to your computer `git clone git@github.com:ThomasMeier/beer.git`
2. Run Ansible `ansible-playbook -i hosts playbook.yml`

This will attempt to install your ssh key if you don't have it set. It will
ask for a password otherwise. You must have Leiningen installed. Beer.jar
will run on start up, and you may need to reboot to get it running.

### Manual Start Up (If not starting on boot)

```bash
ssh pi
screen # Press enter after message shows
sudo java -jar beer.jar
```

Press `CTRL`+`A`+`D` to detach screen. Beer.jar might need 1-10 minutes to start.

### Environment

Must set these variables in the Pi. Add to `/etc/environment`. Example:

```
# Path to sensor file 1
TEMP_1=/sys/bus/drivers/w1/28-03135/w1_slave
# Path to sensor file 2
TEMP_2=/sys/bus/drivers/w1/28-09877/w1_slave
```

Copyright 2016 Thomas Meier
