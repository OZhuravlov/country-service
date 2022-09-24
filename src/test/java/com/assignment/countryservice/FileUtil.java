package com.assignment.countryservice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class FileUtil {

    public static <T> List<T> getListFromJsonFile(final String path, Class<T> clazz) throws IOException {
        final CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return MAPPER.readValue(readDataFromFile(path), javaType);
    }

    public static String readDataFromFile(final String path) throws IOException {
        final Resource stateFile = new ClassPathResource(path);
        try (final Reader reader = new InputStreamReader(stateFile.getInputStream())) {
            return CharStreams.toString(reader);
        }
    }

    private static ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private static final ObjectMapper MAPPER = getObjectMapper();
    public static <T> T getObjectFromJsonFile(final String path, Class<T> clazz) throws IOException {
        return MAPPER.readValue(readDataFromFile(path), clazz);
    }
}
