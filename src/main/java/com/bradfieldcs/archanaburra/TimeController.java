package com.bradfieldcs.archanaburra;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping(path = "/wall-clock-time")
public class TimeController {

    @PostMapping(path = "/calculate")
    public String calculate(@RequestBody Request request) {
        ZoneId zoneId = Optional.ofNullable(request.getZoneId())
            .map(zone -> TimeZone.getTimeZone(zone).toZoneId())
            .orElse(ZoneOffset.UTC);

        return ZonedDateTime.ofInstant(Instant.now(ClockSetter.getClock()), zoneId)
            .toLocalDateTime()
            .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    static class Request {
        private String zoneId;

        public Request() {
            // For serialization
        }

        public Request(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getZoneId() {
            return zoneId;
        }
    }
}
