package com.yen.clusterAdmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cluster.health.check")
public class HealthMonitorProperties {

    private long interval = 30000; // 30 seconds
    private long timeout = 5000;   // 5 seconds
    private int unhealthyThreshold = 3; // 3 consecutive failures
    private boolean enabled = true;

    public long getInterval() { return interval; }
    public void setInterval(long interval) { this.interval = interval; }

    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }

    public int getUnhealthyThreshold() { return unhealthyThreshold; }
    public void setUnhealthyThreshold(int unhealthyThreshold) { this.unhealthyThreshold = unhealthyThreshold; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
