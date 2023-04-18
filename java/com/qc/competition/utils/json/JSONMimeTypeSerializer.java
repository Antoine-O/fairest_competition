package com.qc.competition.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.activation.MimeType;
import java.io.IOException;

/**
 * Created by Antoine on 22/01/2016.
 */


public class JSONMimeTypeSerializer extends JsonSerializer<MimeType> {

    @Override
    public void serialize(MimeType mimeType, JsonGenerator gen, SerializerProvider provider)

            throws IOException {

        if (mimeType != null)
            gen.writeString(mimeType.toString());

    }

}
