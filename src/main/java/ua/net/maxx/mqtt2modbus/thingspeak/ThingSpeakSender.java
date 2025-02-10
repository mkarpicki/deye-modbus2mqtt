package ua.net.maxx.mqtt2modbus.thingspeak;

//import java.net.HttpURLConnection;
//import java.net.URL;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import org.w3c.dom.ranges.RangeException;

public class ThingSpeakSender {

    private class ThingSpeakRequest {
        private final String apiHost = "https://api.thingspeak.com";
        private final int channelId;
        private final String apiKey;
        private final short FIELDS_SIZE = 8; 
        private final String[] fields = new String[FIELDS_SIZE + 1];

        public ThingSpeakRequest(int channelId, String apiKey) {
            this.channelId = channelId;
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

        public void send() {
            StringBuilder url = new StringBuilder(apiHost);
            short i;
            boolean doSend = false;

            url.append("/update?api_key=").append(this.apiKey);
            
            for (i = 1; i <= FIELDS_SIZE; i++) {
                if (!"".equals(fields[i]) && fields[i] != null) {
                    url.append("&field").append(i).append("=").append(fields[i]);
                    doSend = true;
                }
            }
            if (doSend) {

                String sUrl = url.toString();
                
                HttpClient client = HttpClient.newBuilder().build();

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(sUrl))
                    //.timeout(Duration.ofMinutes(2))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
                client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println);  
                
/*
                try {
                    URL obj = new URL(sUrl);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    System.out.println(Integer.toString(responseCode));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
*/
                System.out.println(url.toString());
        
            }
        }
    }

    //private static final Logger logger = LogManager.getLogger();

    private final HashMap<String, Channel> channels; 
    private final HashMap<String, Mapping> mappings;
    private final HashMap<String, ThingSpeakRequest> requests;
    

    public ThingSpeakSender(Mapping[] mappings, Channel[] channels) {
        this.requests = new HashMap<>();
        this.mappings = this.buildMappings(mappings);
        this.channels = this.buildChannels(channels);
    }

    public ThingSpeakRequest getRequest(int channelId) {
        String sChannelId = Integer.toString(channelId);
        if (!this.requests.containsKey(sChannelId)) {
            Channel c = this.channels.get(sChannelId);
            if (c != null) {
                ThingSpeakRequest r = new ThingSpeakRequest(channelId, c.getApiKey());
                this.requests.put(sChannelId, r);    
                return r; 
            } else {
                //todo consider raising exception
                //logger.error("No apiKey for channel: [" + sChannelId + "]");
            }
               
        }        
        return this.requests.get(sChannelId);
    }

    public void add(String topic, String value) {
        Mapping mapping = mappings.get(topic);

        if (mapping != null) {
            int channelId = mapping.getChannelId();
            short channelField = mapping.getChannelField();        
            ThingSpeakRequest request = getRequest(channelId);
    
            if (request != null ) {
                request.setField(channelField, value);
            } else {
                //logger.error("no request to add topic");
            }
            
        } else {
            //logger.info("topic: [" + topic + "] to be ignored (not in config)");
        }

    }

    public void send() {
        this.requests.forEach((key, request) -> {
            request.send();
            request.clear();
        });
    }

    private HashMap<String, Channel> buildChannels(Channel[] channels) {
        HashMap<String, Channel> hashChannels = new HashMap<>();
        short length = ((short) channels.length);
        short i;

        for (i = 0; i < length; i++) {
            Channel c = channels[i];
            hashChannels.put(Integer.toString(c.getChannelId()), c);
        }

        return hashChannels;
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

}
