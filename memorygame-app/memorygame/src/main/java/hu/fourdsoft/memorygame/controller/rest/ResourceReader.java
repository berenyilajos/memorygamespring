package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.parser.interfaces.Parser;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResourceReader {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Parser<Object> yamlObjectParser;

    @Autowired
    private Parser<Object> jsonObjectParser;

    public String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Resource getResource(String path) {
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return resource;
    }

    public String readFileToString(String path) {
        Resource resource = getResource(path);
        return asString(resource);
    }

    public String convertYamlToJson(String yaml) throws MyApplicationException {
        Object obj = null;
        try {
            obj = yamlObjectParser.readValue(yaml);
            return jsonObjectParser.writeValueAsString(obj);
        } catch (Exception e) {
            throw new MyApplicationException(e.getMessage());
        }
    }

}
