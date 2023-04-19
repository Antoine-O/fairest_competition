package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Duncan on 20/06/2016.
 */
public class DurationDeserializer extends JsonDeserializer<Duration>{

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        Duration duration = new Duration(value);
        return duration;
    }

}
