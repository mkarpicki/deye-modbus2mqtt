package ua.net.maxx.mqtt2modbus.timer;

//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.annotations.Nullable;
import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.config.Device;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.thingspeak.ThingSpeakSender;

public class BridgeTask extends TimerTask {

    private final List<DataListener> listeners = new ArrayList<>();

    private final static Logger logger = LoggerFactory.getLogger(BridgeTask.class);

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
                logger.debug("topic: {}, payload: {} " + entry.getKey(), entry.getValue());
                thingSpeakSender.add(entry.getKey(), entry.getValue());
            });
        });
        logger.debug("Task finished");
        thingSpeakSender.send();
    }

    public void addListener(DataListener listener) {
        this.listeners.add(listener);
    }
}
