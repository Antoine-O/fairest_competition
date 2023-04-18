package com.qc.competition.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Duncan on 23/06/2017.
 */
public class JSONObjectSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o instanceof String) {
            jsonGenerator.writeString(o.toString());
//        } else if (o instanceof Collection && !((Collection) o).isEmpty() && ((Collection) o).iterator().next() instanceof JSONObject) {
//            String value = JSONUtils.jsonObjectsToString((Collection<? extends JSONObject>) o);
//            jsonGenerator.writeRawValue(value);
//        } else if (o instanceof JSONObject) {
// //            jsonGenerator.writeObject(o);
//            String value = JSONUtils.jsonObjectToString((JSONObject) o);
//            jsonGenerator.writeRawValue(value);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(o);
            jsonGenerator.writeRawValue(value);
        }
    }
}
