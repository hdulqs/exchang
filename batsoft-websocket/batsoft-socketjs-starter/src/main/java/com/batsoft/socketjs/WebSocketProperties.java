package com.batsoft.socketjs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for webSocket.
 *
 * @author Bat Admin
 */
@Component("webSocketProperties")
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {
    /**
     * point
     */
    private String point = "/socket";
    /**
     * broker
     */
    private String broker="/topic";

    /**
     * 应用路径 /app 默认 /
     */
    private String appDestinationPrefixes="/";

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getAppDestinationPrefixes() {
        return appDestinationPrefixes;
    }

    public void setAppDestinationPrefixes(String appDestinationPrefixes) {
        this.appDestinationPrefixes = appDestinationPrefixes;
    }
}
