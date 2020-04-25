package hu.fourdsoft.memorygame.common.parser.impl;

import hu.fourdsoft.memorygame.common.parser.base.JsonParser;
import org.springframework.stereotype.Component;

@Component
public class JsonObjectParser extends JsonParser<Object> {
    public JsonObjectParser() {
        super(Object.class);
    }
}
