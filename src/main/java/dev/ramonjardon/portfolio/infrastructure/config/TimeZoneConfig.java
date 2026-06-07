package dev.ramonjardon.portfolio.infrastructure.config;

import java.util.TimeZone;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component  
public class TimeZoneConfig {

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
