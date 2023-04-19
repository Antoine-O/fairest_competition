package com.qc.competition.service.structure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Duncan on 20/06/2016.
 */
public class VersionSerializer extends JsonSerializer<Version> {
    @Override
    public void serialize(Version version, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formattedVersion = version.toString();
        jsonGenerator.writeString(formattedVersion);
    }
}
