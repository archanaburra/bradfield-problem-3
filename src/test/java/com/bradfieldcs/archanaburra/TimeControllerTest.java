package com.bradfieldcs.archanaburra;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeControllerTest {

    private static final ZonedDateTime TEST_TIME = ZonedDateTime.of(2020, 12, 30, 17, 15, 13, 0, ZoneOffset.UTC);

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        Clock fixedClock = Clock.fixed(TEST_TIME.toInstant(), TEST_TIME.getZone());
        ClockSetter.setClock(fixedClock);
    }

    @AfterEach
    public void tearDown() {
        ClockSetter.reset();
    }

    @Test
    public void getTime() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wall-clock-time/calculate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("17:15:13")));
    }

    @Test
    public void getTimeInLosAngeles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wall-clock-time/calculate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new TimeController.Request("America/Los_Angeles"))))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("09:15:13")));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
