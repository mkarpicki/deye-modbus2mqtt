package ua.net.maxx.mqtt2modbus;

import org.junit.Ignore;
import org.junit.Test;


@Ignore
public class AppTest {

    @Test
    @Ignore
    public void fullTest() throws Exception {
        // Yaml yaml = new Yaml();
        // InputStream inputStream = new BufferedInputStream(new FileInputStream("config.yaml"));
        // Config config = yaml.loadAs(inputStream, Config.class);

        // SerialParameters portParams = new SerialParameters();
        // portParams.setPortName(config.getModbus().getPort());
        // portParams.setBaudRate(config.getModbus().getRate());
        // portParams.setStopbits(1);
        // portParams.setParity(0);
        // portParams.setEncoding(Modbus.SERIAL_ENCODING_RTU);

        // MqttSender mqttSender = new MqttSenderImpl("tcp://mqtt.sng.maxx");

        // ModbusService modbusService = new ModbusServiceImpl(portParams);
        // config.getDevices().forEach(device -> {
        //     Map<String, String> data = new TreeMap<>(modbusService.getData(device));
        //     data.entrySet().forEach(entry -> {
        //         System.out.println(String.format("%s => %s", entry.getKey(), entry.getValue()));
        //         mqttSender.send(entry.getKey(), entry.getValue());
        //     });

        //     data.entrySet().stream()
        //             .filter(entry -> entry.getKey().contains("total"))
        //             .filter(entry -> entry.getKey().contains("power"))
        //             .forEach(entry -> {
        //                 System.out.println(String.format("%s => %s", entry.getKey(), entry.getValue()));
        //                 /*String topic = entry.getKey();
        //                 String id = "SNG_" + topic.replace("/", "_");
        //                 String item = "  - id: " + id + "\n" +
        //                 "    channelTypeUID: mqtt:number\n" +
        //                 "    label: " + topic.replace("/", "_") + "\n" +
        //                 "    description: \"\"\n" +
        //                 "    configuration:\n" +
        //                 "      stateTopic: " + topic + "\n";
        //                 System.out.println(item);*/
        //             });
        // });


    }

}
