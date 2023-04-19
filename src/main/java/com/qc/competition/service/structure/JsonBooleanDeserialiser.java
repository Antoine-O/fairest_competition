package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex6 on 05/07/2016.
 */
public class JsonBooleanDeserialiser extends JsonDeserializer<Boolean> {
    private static Set<String> validTrueValues = new HashSet<>();


    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String str = jsonParser.getValueAsString();
        Boolean value = Boolean.FALSE;
        try {
            value = Boolean.parseBoolean(str);
        } catch (Exception e) {
            value = str != null && !str.isEmpty();
        }

        return value;
    }
}
