package ua.net.maxx.mqtt2modbus;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.modbus.impl.ModbusServiceImpl;
import ua.net.maxx.mqtt2modbus.thingspeak.Mapping;
import ua.net.maxx.mqtt2modbus.thingspeak.ThingSpeakSender;
import ua.net.maxx.mqtt2modbus.timer.BridgeTask;

public class Starter {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new BufferedInputStream(new FileInputStream("deye_config_lite.yaml"));
            Config config = yaml.loadAs(inputStream, Config.class);

            JsonReader reader = new JsonReader(new FileReader("mapping.json"));
            Gson g = new Gson();
            Mapping[] mapping = g.fromJson(reader, Mapping[].class);

            ThingSpeakSender thingSpeakSender = new ThingSpeakSender(mapping);

            // @todo REMOVE ME AFTER TEST :)
            // thingSpeakSender.add("/battery/soc", "21");
            // thingSpeakSender.add("/battery/temperature", "22");
            // thingSpeakSender.add("/battery/power", "23");
            // thingSpeakSender.add("/grid/out/phase/A/power", "5");
            // thingSpeakSender.add("/inverter/load/phase/A/power", "55.6");
            // thingSpeakSender.send();

            SerialParameters portParams = new SerialParameters();
            portParams.setPortName(config.getModbus().getPort());
            portParams.setBaudRate(config.getModbus().getRate());
            portParams.setStopbits(1);
            portParams.setParity(0);
            portParams.setEncoding(Modbus.SERIAL_ENCODING_RTU);
            //MqttSender mqttSender = null; //new MqttSenderImpl("");
            ModbusService modbusService = new ModbusServiceImpl(portParams);

            //InfluxDBStorageService influxDb = new InfluxDBStorageService(config.getInflux());
            BridgeTask bridgeTask = new BridgeTask(thingSpeakSender, modbusService, config);
            //bridgeTask.addListener(influxDb);
            Timer timer1 = new Timer();
            timer1.schedule(bridgeTask, Starter.getNextStartDate(), 10000);
            logger.info("Bridge Timer scheduled");
        } catch (Exception e) {
            logger.error("Error starting app", e);
        }
    }

    private static Date getNextStartDate() {
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        c.add(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 5);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();

    }
}
