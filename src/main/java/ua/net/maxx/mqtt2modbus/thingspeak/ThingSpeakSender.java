package ua.net.maxx.mqtt2modbus.thingspeak;

import java.util.HashMap;

import org.w3c.dom.ranges.RangeException;

public class ThingSpeakSender {

    private class ThingSpeakRequest {
        private final String apiHost = "https://api.thingspeak.com";
        //private int channelId;
        private String apiKey;
        private String[] fields;
        private final short FIELDS_SIZE = 8; 

        // public void setChannelId(int channelId) {
        //     this.channelId = channelId;
        // }

        public ThingSpeakRequest(String apiKey) {
            this.apiKey = apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
        public void setField(short position, String value) throws RangeException {
            if (position < 1 || position > FIELDS_SIZE) {
                throw new RangeException(position, "is position out of array range (1 - " + FIELDS_SIZE + ")");
            }
            fields[position] = value;
        }

        public void clear() {
            short i;
            for (i = 1; i <= FIELDS_SIZE; i++) {
                fields[i] = null;
            }
        }

        public short send() {
            StringBuilder url = new StringBuilder(apiHost);
            short i;

            url.append("/update?api_key=").append(this.apiKey);
            
            for (i = 1; i <= FIELDS_SIZE; i++) {
                if (!"".equals(fields[i]) && fields[i] != null) {
                    url.append("&field").append(i).append("=").append(fields[i]);
                }
            }
            System.out.println("send");
            System.out.println(url.toString());
            
            return 200;
        }

    }

    //private Mapping[] mappings;
    private final HashMap<String, Mapping> mappings;
    //private List<Mapping> mappings;
    //private ArrayList<Mapping> mappings;
    private final HashMap<String, ThingSpeakRequest> requests;
    

    public ThingSpeakSender(Mapping[] mappings) {
        //this.mappings = mappings;
        //this.mappings = new ArrayList<Mapping>(Arrays.asList(mappings));
        //this.mappings = Arrays.asList(mappings);
        this.requests = new HashMap<>();
        this.mappings = this.buildMappings(mappings);
    }

    public ThingSpeakRequest getRequest(int channelId) {
        String sChannelId = Integer.toString(channelId);
        if (!this.requests.containsKey(sChannelId)) {
            this.requests.put(sChannelId, new ThingSpeakRequest("xxx"));    
        }        
        return this.requests.get(sChannelId);
    }

    public void add(String topic, String value) {
        Mapping mapping = mappings.get(topic);
        int channelId = mapping.getChannelId();
        short channelField = mapping.getChannelField();        
        ThingSpeakRequest request = getRequest(channelId);

        request.setField(channelField, value);
    }

    public void send() {
        this.requests.forEach((key, request) -> {
            request.send();
            request.clear();
        });
    }

    private HashMap<String, Mapping> buildMappings(Mapping[] mappings) {
        HashMap<String, Mapping> hashMappings = new HashMap<>();
        short length = ((short)mappings.length);
        short i;

        for (i = 0; i < length; i++) {
            Mapping m = mappings[i];
            hashMappings.put(m.getTopic(), m);
        }
        return hashMappings;
        
    }

    private void flush() {
        //this.requests.clear();
    }
}
