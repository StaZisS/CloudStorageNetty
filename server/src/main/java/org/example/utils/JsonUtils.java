package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.ResponseTypeEnum;

public class JsonUtils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(Object json, Class<T> clazz) {
        try {
            String jsonString = objectMapper.writeValueAsString(json);
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResponseTypeEnum getTypeResponseEnum(String value) {
        for (ResponseTypeEnum responseTypeEnum : ResponseTypeEnum.values()) {
            if (responseTypeEnum.getValue().equals(value)) {
                return responseTypeEnum;
            }
        }
        return null;
    }
}
