# deye-modbus2mqtt
Initialy created as bridge between DEYE inverter and MQTT broker
Changed to push data to ThingSpeak (multi channels)
Originally Tested with: Deye SUN-12K-SG04LP3-EU
Fork Tested with: Deye SUN-8K-SG04LP3-EU

# Current features
- pull DEYE registers every 15 seconds
- push data to Mathlab's ThingSpeak Channels
- required config files
-- deye-config-lite.json (list of registries and topics to monitor)
-- thingspeak-config.json (list of Thingspeak channels with write apiKeys)
-- deye-thingspeak-mapping.json (mapping of each topic monitored and channel & field to send)

#Install on PRI
- prepare jar (mvn  package)
- prepare config file
- copy jar and  config file to any directory
- create service [How to convert jar file to service](https://dzone.com/articles/run-your-java-application-as-a-service-on-ubuntu)
- define port in deye-config-lite.json (example: windows COM10, linux: /dev/ttyACM0)

# Useful links
https://phoenixnap.com/kb/install-java-raspberry-pi
https://raspberrypi.stackexchange.com/questions/96455/how-do-i-make-a-java-application-i-made-run-as-a-deamon

https://kellerza.github.io/sunsynk/guide/wiring
https://solarenergyconcepts.co.uk/practical-and-diy/crc-error-solar-assistant/
https://github.com/kbialek/deye-inverter-mqtt/blob/main/ha_definitions/deye_hybrid_map.yaml
https://github.com/StephanJoubert/home_assistant_solarman/blob/main/custom_components/solarman/inverter_definitions/deye_sg04lp3.yaml

https://dzone.com/articles/run-your-java-application-as-a-service-on-ubuntu

Status Enum 
0 - standby
1 - selfcheck
2 - normal
3 - alarm
4 - fault

# todo
- rename package from original one as no mqtt integration in place anymore
- delete jar from repo
- log4j (does not update file on each new day)
- test if I did need that mapping (removed due to lack of space in channel)
-- { "channelId": 2814878, "channelField": 2, "topic": "/grid/in/phase/total/power/apparent" },
- this one sends strange values
-- { "channelId": 2814869, "channelField": 3, "topic": "/battery/power" },