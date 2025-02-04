# deye-modbus2mqtt
Initialy created as bridge between DEYE inverter and MQTT broker
Tested with: Deye SUN-12K-SG04LP3-EU

# Current features
- pull DEYE registers every 5 seconds
- push data to MQTT
- push data to InfluxDB

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
https://github.com/kbialek/deye-inverter-mqtt/blob/main/ha_definitions/deye_hybrid_map.yaml




