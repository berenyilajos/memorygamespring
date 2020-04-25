package hu.fourdsoft.memorygame.common.parser.impl;

import hu.fourdsoft.memorygame.common.parser.base.YamlParser;
import org.springframework.stereotype.Component;

@Component
public class YamlObjectParser extends YamlParser<Object> {
    public YamlObjectParser() {
        super(Object.class);
    }
}
