package ua.net.maxx.mqtt2modbus.thingspeak;

public class Channel {

    private int channelId;
    private String apiKey;

    public int getChannelId() {
        return channelId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
