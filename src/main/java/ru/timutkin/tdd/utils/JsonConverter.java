package ru.timutkin.tdd.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonConverter() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String convert(Object object) throws JsonProcessingException {
        return  mapper.writeValueAsString(object);
    }

}
