package hu.fourdsoft.memorygame.common.parser.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.fourdsoft.memorygame.common.parser.interfaces.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class JsonParser<T> implements Parser<T> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private Class<T> parsedClass;

    public JsonParser(Class<T> parsedClass) {
        this.parsedClass = parsedClass;
    }

    public T parse(InputStream input) throws Exception {
        return parseJson(input);
    }

    public void write(T obj, OutputStream output) throws Exception {
        writeJson(obj, output);
    }

    public T readValue(String input) throws IOException {
        return mapper.readValue(input, getParsedClass());
    }

    public String writeValueAsString(T obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    private T parseJson(InputStream input) throws IOException {
        return mapper.readValue(input, getParsedClass());
    }

    private void writeJson(T obj, OutputStream output) throws IOException {
        mapper.writeValue(output, obj);
    }

    public Class<T> getParsedClass() {
        return parsedClass;
    }

    public String getExtension() {
        return ".json";
    }

}
