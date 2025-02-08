package ua.net.maxx.mqtt2modbus.config;

import java.util.List;

public class Config {
    private ModbusPort modbus;

    private List<Device> devices;

    public ModbusPort getModbus() {
        return modbus;
    }

    public void setModbus(ModbusPort modbus) {
        this.modbus = modbus;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}