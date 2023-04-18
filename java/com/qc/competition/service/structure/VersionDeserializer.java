package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by Duncan on 20/06/2016.
 */
public class VersionDeserializer extends JsonDeserializer<Version> {

    @Override
    public Version deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Version version = Version.parse(jsonParser.getText());
        return version;
    }
}
