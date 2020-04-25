package hu.fourdsoft.memorygame.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ResourceReader {

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Resource getResource(String path) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return resource;
    }

    public static String readFileToString(String path) {
        Resource resource = getResource(path);
        return asString(resource);
    }

    public static String convertYamlToJson(String yaml) throws MyApplicationException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = null;
        try {
            obj = yamlReader.readValue(yaml, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();
            return jsonWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new MyApplicationException(e.getMessage());
        }
    }

}
