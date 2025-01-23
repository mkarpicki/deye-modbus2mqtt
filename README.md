# deye-modbus2mqtt
Initialy created as bridge between DEYE inverter and MQTT broker
Tested with: Deye SUN-12K-SG04LP3-EU

# Current features
- pull DEYE registers every 5 seconds
- push data to MQTT
- push data to InfluxDB

# TODO
- Focus on
LOAD
now : sum up
/DEYE/inverter/load/phase/C/power
/DEYE/inverter/load/phase/B/power
/DEYE/inverter/load/phase/A/power

/inverter/stat/power/load/day

GRID
/inverter/stat/grid/power/buy/today
/inverter/stat/grid/power/sell/today
(lub /DEYE/inverter/stat/grid/power/sell/today)

sum now?
/grid/out/phase/A/power
/grid/out/phase/B/power
/grid/out/phase/C/power
/grid/in/phase/A/power
/grid/in/phase/B/power
/grid/in/phase/C/power


battery

/inverter/stat/battery/charge/today
/inverter/stat/battery/discharge/today
/battery/temperature
/battery/soc
/battery/power



#Install on PRI
- prepare jar (mvn  package)
- prepare config file
- copy jar and  config file to any directory
- create service [How to convert jar file to service](https://dzone.com/articles/run-your-java-application-as-a-service-on-ubuntu)

# Grafana dashboard example

![Grafana dashboard example](docs/dashboard_example.png "Grafana dashboard example")

# Useful links

https://kellerza.github.io/sunsynk/guide/wiring
https://solarenergyconcepts.co.uk/practical-and-diy/crc-error-solar-assistant/




