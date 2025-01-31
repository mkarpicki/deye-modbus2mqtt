package ua.net.maxx.mqtt2modbus.thingspeak;

public class Mapping {
    private int channelId;
    private short channelField;
    private String topic;

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setChannelField(short channelField) {
        this.channelField = channelField;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getChannelId() {
        return channelId;
    }

    public short getChannelField() {
        return channelField;
    }

    public String getTopic() {
        return topic;
    }
}

