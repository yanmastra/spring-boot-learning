package com.yanmastra.msSecurityBase.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yanmastra.msSecurityBase.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonUtils {
    private JsonUtils() {
    }

    private static ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final DateFormat DEFAULT_ZONED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            configure(objectMapper);
        }
        return objectMapper;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        if (JsonUtils.objectMapper != null && !(JsonUtils.objectMapper.equals(objectMapper))) {
            JsonUtils.objectMapper = objectMapper;
            configure(JsonUtils.objectMapper);
        }
    }

    private static void configure(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        objectMapper.setDateFormat(DEFAULT_ZONED_DATE_FORMAT);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object object, boolean throwException) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            if (throwException) throw new RuntimeException(e);
            else {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public static String toJson(Object object) {
        return toJson(object, false);
    }

    public static <E> E fromJson(String json, TypeReference<E> typeReference, boolean throwException) {
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            if (throwException) throw new RuntimeException(e);
            else {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public static <E> E fromJson(String json, Class<E> eClass, boolean throwException) {
        try {
            return getObjectMapper().readValue(json, eClass);
        } catch (JsonProcessingException e) {
            if (throwException) throw new RuntimeException(e);
            else {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public static <E> E fromJson(String json, Class<E> eClass) {
        return fromJson(json, eClass, false);
    }

    public static <E> E fromJson(String json, TypeReference<E> typeReference) {
        return fromJson(json, typeReference, false);
    }

    public static boolean isJson(String s) {
        try {
            objectMapper.readTree(s);
        } catch (JacksonException je) {
            Log.log.warn(je.getMessage());
            return false;
        }
        return true;
    }
}
