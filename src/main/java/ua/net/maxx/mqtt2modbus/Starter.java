package ua.net.maxx.mqtt2modbus;

import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

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

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

    //private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            /*
            Yaml yaml = new Yaml();
            InputStream inputStream = new BufferedInputStream(new FileInputStream("deye_config_lite.yaml"));
            Config config = yaml.loadAs(inputStream, Config.class);
            */
            
            Gson g = new Gson();
            JsonReader reader;
            
            reader = new JsonReader(new FileReader("deye-config-lite.json")); 
            Config config = g.fromJson(reader, Config.class);

            reader = new JsonReader(new FileReader("deye-thingspeak-mapping.json"));            
            Mapping[] mapping = g.fromJson(reader, Mapping[].class);

            reader = new JsonReader(new FileReader("thingspeak-config.json"));  
            Channel[] channels = g.fromJson(reader, Channel[].class); 

            reader.close();

            ThingSpeakSender thingSpeakSender = new ThingSpeakSender(mapping, channels);

            // // // @todo REMOVE ME AFTER TEST :)
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
            
            start(portParams, thingSpeakSender, config);

            //logger.info("Bridge Timer scheduled");
        } catch (Exception e) {
            //logger.error("Error starting app", e);
        }
    }

    private static void start(SerialParameters portParams, ThingSpeakSender thingSpeakSender, Config config) throws Exception {

        short i = 0;
        short tries = 3;
        short delayInSeconds = 3 * 1000;
        short modbusTimeScheduleInSeconds = 15 * 1000;

        while(true) {
            try {
                ModbusService modbusService = new ModbusServiceImpl(portParams);

                BridgeTask bridgeTask = new BridgeTask(thingSpeakSender, modbusService, config);
                Timer timer1 = new Timer();
                timer1.schedule(bridgeTask, Starter.getNextStartDate(), modbusTimeScheduleInSeconds);
                return;
            } catch (Exception e) {
                // handle exception
                Thread.sleep(delayInSeconds);
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
