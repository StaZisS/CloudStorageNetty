package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T decodeJson(Object json, TypeReference<T> clazz) {
        try {
            String jsonString = objectMapper.writeValueAsString(json);
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
