package com.qc.competition.service.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qc.competition.utils.Counter;

import java.io.IOException;

/**
 * Created by alex6 on 05/07/2016.
 */
public class CounterSerializer extends JsonSerializer<Counter> {

    @Override
    public void serialize(Counter counter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        int value= b ? 1 : 0;
//        jsonGenerator.writeNumber(value);
        String str = "" + counter.value;
        jsonGenerator.writeString(str);
    }
}
