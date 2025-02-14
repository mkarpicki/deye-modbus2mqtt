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

# Useful links

https://kellerza.github.io/sunsynk/guide/wiring
https://solarenergyconcepts.co.uk/practical-and-diy/crc-error-solar-assistant/
https://github.com/kbialek/deye-inverter-mqtt/blob/main/ha_definitions/deye_hybrid_map.yaml
https://github.com/StephanJoubert/home_assistant_solarman/blob/main/custom_components/solarman/inverter_definitions/deye_sg04lp3.yaml
https://dzone.com/articles/run-your-java-application-as-a-service-on-ubuntu

# todo
- rename package from original one as no mqtt integration in place anymore
- consider moving "status"(500) reg to separate channel with alerts (when I confirm I get other alerts and channel is properly utilized, for now keep it with inverter & power channel fields)
- add env to read path
- delete jar from repo