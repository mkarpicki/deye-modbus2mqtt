package ua.net.maxx.mqtt2modbus;

import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ua.net.maxx.mqtt2modbus.config.Config;
import ua.net.maxx.mqtt2modbus.modbus.ModbusService;
import ua.net.maxx.mqtt2modbus.modbus.impl.ModbusServiceImpl;
import ua.net.maxx.mqtt2modbus.thingspeak.Channel;
import ua.net.maxx.mqtt2modbus.thingspeak.Mapping;
import ua.net.maxx.mqtt2modbus.thingspeak.ThingSpeakSender;
import ua.net.maxx.mqtt2modbus.timer.BridgeTask;

public class Starter {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);
    private static String configPath = "";

    private static String getConfigPath(String fileName) {
        return configPath + fileName;
    } 

    public static void main(String[] args) {

        try {

            if (args.length > 0) {
                configPath = (args[0].isEmpty()) ? "" : args[0];
            }

            logger.debug("configPath: " + configPath);    
            
            Gson g = new Gson();
            JsonReader reader;
            
            reader = new JsonReader(new FileReader(getConfigPath("deye-config-lite.json"))); 
            Config config = g.fromJson(reader, Config.class);

            reader = new JsonReader(new FileReader(getConfigPath("deye-thingspeak-mapping.json")));            
            Mapping[] mapping = g.fromJson(reader, Mapping[].class);

            reader = new JsonReader(new FileReader(getConfigPath("thingspeak-config.json")));  
            Channel[] channels = g.fromJson(reader, Channel[].class); 

            reader.close();

            ThingSpeakSender thingSpeakSender = new ThingSpeakSender(mapping, channels);

            // // @todo REMOVE ME AFTER TEST :)
            // thingSpeakSender.add("/battery/soc", "21");
            // thingSpeakSender.add("/battery/sox", "-21");
            // thingSpeakSender.add("/battery/temperature", "10");
            // thingSpeakSender.send();
            // thingSpeakSender.add("/battery/power", "23W");
            // thingSpeakSender.add("/grid/out/phase/A/power", "50");
            // thingSpeakSender.add("/inverter/load/phase/A/power", "55.6");
            // thingSpeakSender.send();

            // thingSpeakSender.add("/battery/soc", "44");
            // thingSpeakSender.send();

            SerialParameters portParams = new SerialParameters();
            portParams.setPortName(config.getModbus().getPort());
            portParams.setBaudRate(config.getModbus().getRate());
            portParams.setStopbits(1);
            portParams.setParity(0);
            portParams.setEncoding(Modbus.SERIAL_ENCODING_RTU);
            
            //tryToStart(portParams, thingSpeakSender, config);
            start(portParams, thingSpeakSender, config);

            logger.info("Bridge Timer scheduled");
        } catch (Exception e) {
            logger.error("Error starting app " + e.getMessage());
        }
    }

    private static void start(SerialParameters portParams, ThingSpeakSender thingSpeakSender, Config config) throws Exception {
        int modbusTimeScheduleInSeconds = 60 * 1000;
        ModbusService modbusService = new ModbusServiceImpl(portParams);

        BridgeTask bridgeTask = new BridgeTask(thingSpeakSender, modbusService, config);
        Timer timer1 = new Timer();
        timer1.schedule(bridgeTask, Starter.getNextStartDate(), modbusTimeScheduleInSeconds);
    }

    private static void tryToStart(SerialParameters portParams, ThingSpeakSender thingSpeakSender, Config config) throws Exception {

        short i = 0;
        short tries = 3;
        short retryOnStartFailureDelayInSeconds = 3 * 1000;

        while(true) {
            try {
                start(portParams, thingSpeakSender, config);
                return;
            } catch (Exception e) {
                // handle exception
                Thread.sleep(retryOnStartFailureDelayInSeconds);
                if (++i == tries) throw e;
            }
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
