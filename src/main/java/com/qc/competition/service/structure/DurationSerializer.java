package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Duncan on 20/06/2016.
 */
public class DurationSerializer extends JsonSerializer<Duration>{
    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (duration != null) {
            String formattedDuration = duration.toString();
            jsonGenerator.writeString(formattedDuration);
        } else {
//            jsonGenerator.writeNull();
        }
    }
}
