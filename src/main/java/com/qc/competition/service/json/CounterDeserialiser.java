package com.qc.competition.service.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.qc.competition.service.structure.Counter;

import java.io.IOException;

/**
 * Created by alex6 on 05/07/2016.
 */
public class CounterDeserialiser extends JsonDeserializer<Counter> {

    @Override
    public Counter deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String str = jsonParser.getValueAsString();
        Counter counter = null;
        if (str != null && !str.trim().isEmpty()) {
            counter = new Counter();
            counter.value = Integer.parseInt(str);
        }
        return counter;
    }
}
