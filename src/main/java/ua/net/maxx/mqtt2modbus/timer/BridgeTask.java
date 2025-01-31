package ua.net.maxx.mqtt2modbus.timer;

//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.reactivex.annotations.Nullable;
import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.config.Device;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.thingspeak.ThingSpeakSender;

public class BridgeTask extends TimerTask {

    private final List<DataListener> listeners = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

    @Nullable
    //private final MqttSender mqttSender;
    private final ModbusService modbusService;
    private final ThingSpeakSender thingSpeakSender;
    private final List<Device> devices;

    public BridgeTask(ThingSpeakSender thingSpeakSender, ModbusService modbusService, Config config) {
        //this.mqttSender = mqttSender;
        this.thingSpeakSender = thingSpeakSender;
        this.modbusService = modbusService;
        this.devices = config.getDevices();
    }

    @Override
    public void run() {
        logger.debug("Task started");
        devices.forEach(device -> {
            Map<String, String> data = modbusService.getData(device);
            data.entrySet().forEach(entry -> {
                listeners.forEach(listener -> listener.publish(entry.getKey(), entry.getValue()));
                //mqttSender.send(entry.getKey(), entry.getValue());
                logger.trace("topic: {}, payload: {}", entry.getKey(), entry.getValue());
                System.out.println("topic: {}, payload: {} " + entry.getKey() + " " + entry.getValue());
                thingSpeakSender.add(entry.getKey(), entry.getValue());
            });
        });
        logger.debug("Task finished");
        System.out.println("Task finished");
        System.out.println("");
        thingSpeakSender.send();
    }

    public void addListener(DataListener listener) {
        this.listeners.add(listener);
    }
}
