package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by alex6 on 05/07/2016.
 */
public class JsonBooleanSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean b, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        int value= b ? 1 : 0;
//        jsonGenerator.writeNumber(value);
        jsonGenerator.writeBoolean(b != null && b);
    }
}
