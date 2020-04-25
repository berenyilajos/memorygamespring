package hu.fourdsoft.memorygame.common.parser.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

public interface Parser<T> {

    T parse(InputStream input) throws Exception;

    void write(T obj, OutputStream output) throws Exception;

    T readValue(String input) throws Exception;

    String writeValueAsString(T obj) throws Exception;

    String getExtension();

}
